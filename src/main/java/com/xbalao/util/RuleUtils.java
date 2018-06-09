package com.xbalao.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.cf88.service.exception.CFException;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class RuleUtils {
	
	private static String[] keys=new String[]{"root","shortImg","title","remark","ctime_str","viewers"};
	
	private  Map<String,Object> jsonMap = new HashMap<String,Object>(); 
	public  static RuleUtils getInstance(String json)
	{
		return new RuleUtils(json);
	}
	
	private RuleUtils(String json)
	{
		parseJSON2Map(json, null);
	}
	/**
     * JSON 类型的字符串转换成 Map
     */
    public  void parseJSON2Map(String jsonStr,String parentKey){
        //字符串转换成JSON对象
        JSONObject json = JSONObject.fromObject(jsonStr);
        //最外层JSON解析
        for(Object k : json.keySet()){
            //JSONObject 实际上相当于一个Map集合，所以我们可以通过Key值获取Value
            Object v = json.get(k);
            //构造一个包含上层keyName的完整keyName
            String fullKey = (null == parentKey || parentKey.trim().equals("") ? k.toString() : parentKey + "." + k);

            if(v instanceof JSONArray){
                 //如果内层还是数组的话，继续解析
                Iterator<?> it = ((JSONArray) v).iterator();
                while(it.hasNext()){
                    JSONObject json2 = (JSONObject)it.next();
                    parseJSON2Map(json2.toString(),fullKey);
                }
            } else if(isNested(v)){
                parseJSON2Map(v.toString(),fullKey);
            }
            else{
                jsonMap.put(fullKey, v);
            }
        }
    }
	
    public  boolean isNested(Object jsonObj){

        return jsonObj.toString().contains("{");
    }
    
    public  String getJsonValue(String key)
    {
    	Object ob =jsonMap.get(key);
    	if(ob==null) return null;
    	return ob.toString();
    }
    
	private boolean isJson(String elem)
	{
		for (String key : keys) {
			if(key.equalsIgnoreCase(elem))
				return true;
		}
		return false;
	}
	
	
	public  Elements  getElements(Element document,String key)
	{
		String target[] =getJsonValue(key+".target").split("[//|]");
		String rootTargetName =target[0];
		String rootTargetValue =target[1];
		Elements elements=null;
		if("tag".equalsIgnoreCase(rootTargetName))
		{
			elements= document.getElementsByTag(rootTargetValue);
		}
		else if("css".equalsIgnoreCase(rootTargetName))
		{
			elements= document.getElementsByClass(rootTargetValue);
		}
		else if("id".equalsIgnoreCase(rootTargetName))
		{
			elements= new Elements(document.getElementById(rootTargetValue));
		}
		else if("attr".equalsIgnoreCase(rootTargetName))
		{
			String attrs[]=rootTargetValue.split("=");
			elements= document.getElementsByAttributeValue(attrs[0], attrs[1]);
		}
		else 
			throw new CFException("没有找到类型");
		if(target.length==3)
		{
			if(StringUtils.isNotBlank(target[2]))
			{
				String tag=target[2].trim();
				if(!NumberUtils.isNumber(tag))
				{
					List<Element> result = new ArrayList<Element>();
					for (Element element : elements) {
						Elements childreEls = element.getElementsByTag(tag);
						for (Element childreElement : childreEls) {
							result.add(childreElement);
						}
					}
					return new Elements(result);
				}
			}
		}
		return elements;
	}
	/**
	 * attr|disable=true|0
	 * @param elements
	 * @param check
	 * @return
	 */
	public String getByAttr(Element parent,String articleFiled)
	{
		return getByElements( parent,articleFiled,"attr");
	}
	
	private String getByElements(Element parent,String articleFiled,String type)
	{
		
		String checkElements[] = getJsonValue("root."+articleFiled+".target").split("[//|]");
		if(checkElements.length<2) throw  new CFException("target.length must >=2");
		
		Elements elements =null;
		if("attr".equalsIgnoreCase(type))
		{
			String keyValue[]=checkElements[1].split("=");
			elements =parent.getElementsByAttributeValue(keyValue[0], keyValue[1]);
		}
		else if("css".equalsIgnoreCase(type))
			 elements =parent.getElementsByClass(checkElements[1]);
		else if("tag".equalsIgnoreCase(type))
			 elements =parent.getElementsByTag(checkElements[1]);
		
		Element element=null; 
		int index=-1;
		if(checkElements.length==2)
		{
			index=0;
			element=elements.get(index);
		}
		else if(checkElements.length>=3)
		{
			if(StringUtils.isBlank(checkElements[2]) || NumberUtils.isNumber(checkElements[2].trim()))
			{
				if(StringUtils.isBlank(checkElements[2]))
					index=0;
				if(NumberUtils.isNumber(checkElements[2].trim()))
					index=Integer.parseInt(checkElements[2].trim());
				element=elements.get(index);
			}
			else element= elements.get(0).getElementsByTag(checkElements[2]).get(0);
		}
		return getElementsValue(element,articleFiled);
	
	}
	
	/**
	 * css|_liad|0
	 * @param elements
	 * @param check
	 * @return
	 */
	public String getByCss(Element parent,String articleFiled)
	{
		return getByElements( parent,articleFiled,"css");
	}
	
	/**
	 * tag|_liad|0
	 * @param elements
	 * @param check
	 * @return
	 */
	public String getByTag(Element parent,String articleFiled)
	{
		return getByElements( parent,articleFiled,"tag");
	}
	
	public String getElementsValue(Element element,String articleFiled)
	{
		String val=getJsonValue("root."+articleFiled+".val");
		if("text".equalsIgnoreCase(val))
		{
			return element.text();
		}
		else if("html".equalsIgnoreCase(val))
		{
			return element.html();
		}
		else return element.attr(val);
	}

	public String getElement(Element parent,String articleFiled)
	{
		String target=getJsonValue("root."+articleFiled+".target");
		//Elements elements =getElements(parent,"root."+articleFiled);
		if(target.toLowerCase().startsWith("tag"))
		{
			return getByTag(parent, articleFiled);
		}
		else if(target.toLowerCase().startsWith("css"))
		{
			return getByCss(parent, articleFiled);
		}
		else if(target.toLowerCase().startsWith("attr"))
		{
			return getByAttr(parent, articleFiled);
		}
		return null;
	}
	
}
