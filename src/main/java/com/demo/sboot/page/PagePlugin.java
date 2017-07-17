package com.demo.sboot.page;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.executor.ExecutorException;
import org.apache.ibatis.executor.statement.BaseStatementHandler;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ParameterMode;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.property.PropertyTokenizer;
import org.apache.ibatis.scripting.xmltags.ForEachSqlNode;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.TypeHandlerRegistry;

import com.demo.sboot.util.ReflectUtil;
import com.demo.sboot.util.StringUtil;



/**
 * 
 * 类描述：自定义拦截器，实现mybatis分页处理
 * com.watcher.page  PagePlugin
 * Created by 78098 on 2016年9月5日.
 * version 1.0
 */
/*
 *  Mybatis拦截器只能拦截四种类型的接口：Executor、StatementHandler、ParameterHandler和ResultSetHandler。Mybatis可以对这四个接口中所有的方法进行拦截。
 * 在Mybatis中Statement语句是通过RoutingStatementHandler对象的prepare方法生成的。所以利用拦截器实现Mybatis分页的一个思路就是拦截StatementHandler接口的prepare方法，然后在拦截器方法中把Sql语句改成对应的分页查询Sql语句，之后再调用StatementHandler对象的prepare方法，即调用invocation.proceed()。
 * 包括sql等其他属性在内的多个属性都没有对应的方法可以直接取到，它们对外部都是封闭的，是对象的私有属性，所以这里就需要引入反射机制来获取或者更改对象的私有属性的值了
 * 对于StatementHandler其实只有两个实现类，一个是RoutingStatementHandler，另一个是抽象类BaseStatementHandler，BaseStatementHandler有三个子类，分别是SimpleStatementHandler，PreparedStatementHandler和CallableStatementHandler，
 * SimpleStatementHandler是用于处理Statement的，PreparedStatementHandler是处理PreparedStatement的，而CallableStatementHandler是处理CallableStatement的。
 * Mybatis在进行Sql语句处理的时候都是建立的RoutingStatementHandler，而在RoutingStatementHandler里面拥有一个  StatementHandler类型的delegate属性，
 * RoutingStatementHandler会依据Statement的不同建立对应的BaseStatementHandler，即SimpleStatementHandler、  PreparedStatementHandler或CallableStatementHandler，
 * 在RoutingStatementHandler里面所有StatementHandler接口方法的实现都是调用的delegate对应的方法。  我们在Interceptor类上已经用@Signature标记了该Interceptor只拦截StatementHandler接口的prepare方法，
 * 又因为Mybatis只有在建立RoutingStatementHandler的时候  是通过Interceptor的plugin方法进行包裹的，所以我们这里拦截到的目标对象肯定是RoutingStatementHandler对象。 
 */
@Intercepts({@Signature(type=StatementHandler.class,method="prepare",args={Connection.class,Integer.class})})
public class PagePlugin implements Interceptor{
	private String dialect="";
	private String pageSqlId="";

	/**
	 * intercept方法就是要进行拦截的时候要执行的方法
	 * 拦截原始语句，查询符合条件的总记录数，重组分页查询语句，然后继续由mybatis处理
	 */
	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		// TODO Auto-generated method stub
		if(invocation.getTarget() instanceof RoutingStatementHandler){
			RoutingStatementHandler statementHandler = (RoutingStatementHandler)invocation.getTarget();
			//
			BaseStatementHandler delegate = (BaseStatementHandler) ReflectUtil.getFieldValue(statementHandler, "delegate");
			MappedStatement mappedStatement = (MappedStatement)ReflectUtil.getFieldValue(delegate, "mappedStatement");//通过反射获取delegate父类BaseStatementHandler的mappedStatement属性  
			 //
			if(mappedStatement.getId().matches(pageSqlId)){   //需要分页处理的
				BoundSql boundSql = delegate.getBoundSql();
				Object parameterObject = boundSql.getParameterObject();//分页SQL<select>中parameterType属性对应的实体参数，即Mapper接口中执行分页方法的参数,该参数不得为空
				if(parameterObject==null){
					throw new NullPointerException("parameterObject尚未实例化！");
				}else{
					Connection connection = (Connection) invocation.getArgs()[0];
					String sql = boundSql.getSql();//原sql语句
					//查询总数时，去掉order by操作
					if(sql.indexOf("order")>0){
						sql=sql.substring(sql.indexOf("order"));
					}
					
					String countSql = "select count(0) from (" + sql+ ")  tmp_count";
					
					PreparedStatement countStmt = connection.prepareStatement(countSql);
					//处理参数
					BoundSql countBS = new BoundSql(mappedStatement.getConfiguration(),countSql,boundSql.getParameterMappings(),boundSql.getParameterObject());
					setParameters(countStmt,mappedStatement,countBS,parameterObject);
					//记录总数
					ResultSet rs = countStmt.executeQuery();
					int count = 0;
					if (rs.next()) {
						count = rs.getInt(1);
					}
					rs.close();
					countStmt.close();
					//参数只接受Page
					Page page=(Page) parameterObject;
					page.setTotalRecord(count);
					//继续处理具体的分页数据查询
					ReflectUtil.setFieldValue(boundSql, "sql",getPagedSql(sql, page));
				}
			}
		}
		return invocation.proceed();
	}

	/**
	 * 在plugin方法中我们可以决定是否要进行拦截进而决定要返回一个什么样的目标对象.
	 * Mybatis中有一个叫做Plugin的类，里面有一个静态方法wrap(Object target,Interceptor interceptor)，通过该方法可以决定要返回的对象是目标对象还是对应的代理
	 */
	@Override
	public Object plugin(Object arg0) {
		return Plugin.wrap(arg0, this);
	}

	/**
	 * setProperties方法是用于在Mybatis配置文件中指定一些属性的
	 */
	@Override
	public void setProperties(Properties arg0){
		try {
			dialect=arg0.getProperty("dialect");
			if(StringUtil.isEmpty(dialect)){
				throw new Exception("Mybatis plugin property(dialect) is null");
			}
			pageSqlId=arg0.getProperty("pageSqlId");
			if(StringUtil.isEmpty(pageSqlId)){
				throw new Exception("Mybatis plugin property(pageSqlId) is null");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取分页语句
	 * @param sql
	 * @param page
	 * @return
	 */
	private String getPagedSql(String sql,Page page){
		StringBuffer pageSql = new StringBuffer();
		if("oracle".equals(dialect)){
			pageSql.append("select * from (select t1.*,ROWNUM row_id from (");
			pageSql.append(sql);
			pageSql.append(") t1 where ROWNUM<=");
			pageSql.append(page.getCurrentRecord()+page.getPageSize());
			pageSql.append(") where row_id>");
			pageSql.append(page.getCurrentRecord());
		}else if("mysql".equals(dialect)){
			pageSql.append("select * from ( ");
			pageSql.append(sql);
			pageSql.append(") as tmp limit ");
			pageSql.append(page.getCurrentRecord());
			pageSql.append(",");
			pageSql.append(page.getCurrentRecord()+page.getPageSize());
		}
		return pageSql.toString();
	}
	
	
	/**
	 * 对SQL参数(?)设值,参考org.apache.ibatis.executor.parameter.DefaultParameterHandler
	 * @param ps
	 * @param mappedStatement
	 * @param boundSql
	 * @param parameterObject
	 * @throws SQLException
	 */
	@SuppressWarnings("all")
	private void setParameters(PreparedStatement ps,MappedStatement mappedStatement,BoundSql boundSql,Object parameterObject) throws SQLException {
		ErrorContext.instance().activity("setting parameters").object(mappedStatement.getParameterMap().getId());
		List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
		if (parameterMappings != null) {
			Configuration configuration = mappedStatement.getConfiguration();
			TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
			MetaObject metaObject = parameterObject == null ? null: configuration.newMetaObject(parameterObject);
			for (int i = 0; i < parameterMappings.size(); i++) {
				ParameterMapping parameterMapping = parameterMappings.get(i);
				if (parameterMapping.getMode() != ParameterMode.OUT) {
					Object value;
					String propertyName = parameterMapping.getProperty();
					PropertyTokenizer prop = new PropertyTokenizer(propertyName);
					if (parameterObject == null) {
						value = null;
					} else if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
						value = parameterObject;
					} else if (boundSql.hasAdditionalParameter(propertyName)) {
						value = boundSql.getAdditionalParameter(propertyName);
					} else if (propertyName.startsWith(ForEachSqlNode.ITEM_PREFIX)&& boundSql.hasAdditionalParameter(prop.getName())) {
						value = boundSql.getAdditionalParameter(prop.getName());
						if (value != null) {
							value = configuration.newMetaObject(value).getValue(propertyName.substring(prop.getName().length()));
						}
					} else {
						value = metaObject == null ? null : metaObject.getValue(propertyName);
					}
					TypeHandler typeHandler = parameterMapping.getTypeHandler();
					if (typeHandler == null) {
						throw new ExecutorException("There was no TypeHandler found for parameter "+ propertyName + " of statement "+ mappedStatement.getId());
					}
					typeHandler.setParameter(ps, i + 1, value, parameterMapping.getJdbcType());
				}
			}
		}
	}

}
