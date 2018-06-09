package com.xbalao.util;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.action.delete.DeleteRequestBuilder;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import com.cf88.model.mysql.mis.vo.Page;
import com.cf88.service.mis.postgre.GtsDataChange;
import com.xbalao.model.vo.ConfigData;

public class TransportClientUtils {
	public  Logger log = LogManager.getLogger(TransportClientUtils.class); 
	private String host="127.0.0.1";
	private int port=9200;
	private  String index="qukuailian", type="article";
	TransportClient client ;
	private int pageSize=20;
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public TransportClient getClient() {
		return client;
	}
	public TransportClientUtils(String host,int port)
	{
		super();
		this.host=host;
		this.port=port;
	}
	public TransportClientUtils()
	{
		this.host=ConfigData.getHost();
		this.port=ConfigData.getPort();
	}
	
	public TransportClientUtils build() {
		try {
			client = new PreBuiltTransportClient(Settings.EMPTY).addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(this.host), this.port));
		} catch (Exception e) {
			log.error("error->{}",e.getMessage());
		}
		return this;
	}
	
	public void close()
	{
		if(client!=null)
			client.close();
	}
	/**
	 * 创建
	 * @param map,里面只要放三个key:id,tital,remark
	 * @return
	 */
	public IndexResponse save(Map<String,?> map)
	{
		try {
			return  client.prepareIndex(index, type, GtsDataChange.obToString(map.get("id"), "0")).setSource(map).get();
		} catch (Exception e) {
			log.error("error->{}",e.getMessage());
		}
		return null;
	}
	/**
	 * 查询操作
	 * @param timeout
	 * @return
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 */
	public Map<String,Object> get(String id) throws InterruptedException, ExecutionException
	{
		GetResponse  getResponse  =client.prepareGet(index, type, id).execute().get();
		return getResponse.getSource();
	}
	
	/**
	 * 分页查询
	 * @param keywords
	 * @param page
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Page getMulti(String keywords,int page)
	{
		MatchQueryBuilder queryBuilder1 =QueryBuilders.matchQuery("title",keywords).analyzer("ik_smart").boost(10);  
		MatchQueryBuilder queryBuilder2 =QueryBuilders.matchQuery("remark",keywords).analyzer("ik_smart").boost(10);  
		BoolQueryBuilder boolQueryBuilder=QueryBuilders.boolQuery(); 
		boolQueryBuilder.should(queryBuilder2).should(queryBuilder1);
		
		SearchResponse response=client.prepareSearch(index).setTypes(type) 
				.setQuery(boolQueryBuilder)
                .setSize(1)  
                .get();  
		long total=  response.getHits().totalHits();
		for (SearchHit  searchHit: response.getHits()) {
        	System.out.println(searchHit.getId());
        } 
		if(total>0)
		{
			 response=client.prepareSearch(index).setTypes(type)  
		              .setQuery(boolQueryBuilder).addSort(FieldSortBuilder.DOC_FIELD_NAME, SortOrder.DESC)
		              .setFrom((page-1)*getPageSize()).setSize(getPageSize())  
		              .setExplain(true)  
		              .get(); 
			
			List<Integer> list = new ArrayList<Integer>();
	        for (SearchHit  searchHit: response.getHits()) {
	        	list.add(Integer.parseInt(searchHit.id()));
	        } 
	        if(list.size()<=0) return null;
	        Page pager = new Page();
	        pager.setData(list);
	        pager.setRecordsTotal((int) total);
	        return pager;
		}
		return null;
	}
	
	
	/**
	 * 删除
	 * @param map
	 * @return
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 */
	public DeleteResponse delete(String id) throws InterruptedException, ExecutionException
	{
		DeleteResponse deleteResponse= client.prepareDelete(index, type,id).execute().get();
		log.info("id->{},index->{}",deleteResponse.getId(),deleteResponse.getIndex());
		return deleteResponse;
	}
	/**
	 * 更新
	 * @param source
	 * @return
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 */
	public UpdateResponse udpate(Map<String,?> source) throws InterruptedException, ExecutionException
	{
		UpdateRequest updateRequest = new UpdateRequest("index", "type", GtsDataChange.obToString(source.get("id")));
		updateRequest.doc(source);
		return client.update(updateRequest).get();
	}
}
