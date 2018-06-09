package com.xbalao.test;


import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.cf88.model.mysql.m.po.MSettingConfig;
import com.cf88.model.mysql.m.vo.StaticConfig;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:spring-service-test.xml"})
public abstract class BaseTest {
	
	public void setTestStatic()
	{
		MSettingConfig mSettingConfig = new MSettingConfig();
		mSettingConfig.setAppId("7759712985148468");
		mSettingConfig.setApi("http://cf.cms.beta.5kwords.com:8080/jinDao");
		mSettingConfig.setAppKey("pcxbjGnpwM6h493nqGTyB23nY8Ymu0fy");
		mSettingConfig.setAdId(3);
		mSettingConfig.setBigPnId(6);
		mSettingConfig.setSmallPnId(5);
		mSettingConfig.setBannerId(7);
		mSettingConfig.setmBannerId(8);
		mSettingConfig.setmPromotionId(9);
		mSettingConfig.setAppBannerId(10);
		mSettingConfig.setAppPromotionId(11);
		mSettingConfig.setFlashTypeId("1,2,3");
		StaticConfig.setSettingConfig(mSettingConfig);
	}
	public void setTrueStatic()
	{
		MSettingConfig mSettingConfig = new MSettingConfig();
		mSettingConfig.setAppId("3836573563872210");
		mSettingConfig.setApi("http://cf.cms.5kwords.com");
		mSettingConfig.setAppKey("d3MnsgMtCdagCshWTcAMymwb4VzJJE0M");
		mSettingConfig.setAdId(4);
		mSettingConfig.setBigPnId(6);
		mSettingConfig.setSmallPnId(5);
		mSettingConfig.setBannerId(7);
		mSettingConfig.setmBannerId(8);
		mSettingConfig.setmPromotionId(9);
		mSettingConfig.setAppBannerId(10);
		mSettingConfig.setAppPromotionId(11);
		mSettingConfig.setFlashTypeId("1,2,3");
		StaticConfig.setSettingConfig(mSettingConfig);
	}
	
	
}
