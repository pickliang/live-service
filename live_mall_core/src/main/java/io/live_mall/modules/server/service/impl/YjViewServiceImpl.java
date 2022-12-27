package io.live_mall.modules.server.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import cn.hutool.core.date.DateUtil;
import io.live_mall.common.utils.PageUtils;
import io.live_mall.common.utils.Query;
import io.live_mall.modules.server.dao.OrgBountyDao;
import io.live_mall.modules.server.dao.OrgTaskDao;
import io.live_mall.modules.server.dao.OrgTaskDetailDao;
import io.live_mall.modules.server.dao.SysOrgUserDao;
import io.live_mall.modules.server.dao.YjViewDao;
import io.live_mall.modules.server.entity.OrgBountyEntity;
import io.live_mall.modules.server.entity.OrgTaskDetailEntity;
import io.live_mall.modules.server.entity.OrgTaskEntity;
import io.live_mall.modules.server.entity.YjViewEntity;
import io.live_mall.modules.server.service.YjViewService;
import io.live_mall.modules.sys.dao.SysUserDao;
import io.live_mall.modules.sys.entity.SysUserEntity;

@Service("yjViewService")
public class YjViewServiceImpl extends ServiceImpl<YjViewDao, YjViewEntity> implements YjViewService {

	@Autowired
	OrgTaskDao orgTaskDao;

	@Autowired
	OrgTaskDetailDao orgTaskDetailDao;

	@Autowired
	OrgBountyDao orgBountyDao;

	@Autowired
	SysOrgUserDao orgUserDao;

	@Autowired
	SysUserDao userDao;

	@Override
	public PageUtils queryPage(Map<String, Object> params) {
		QueryWrapper<YjViewEntity> queryWrapper = new QueryWrapper<YjViewEntity>();
		if (params.get("key") != null && !"".equals(params.get("key"))) {
			queryWrapper.like("user_name", params.get("key"));
		}
		IPage<YjViewEntity> page = this.baseMapper.selectYjPage(new Query<YjViewEntity>().getPage(params), params);
		page.getRecords().stream().forEach(e->{
			e.setWcl(e.getWcl().multiply(new BigDecimal(100)));
		});
		return new PageUtils(page);
	}

	private int getYeji(OrgTaskDetailEntity detail, Integer level, Integer grade) {
		int money = 0;
		String dataStr = detail.getDataStr();
		List<JSONObject> jsonList = JSONObject.parseArray(dataStr, JSONObject.class);
		for (JSONObject object : jsonList) {
			if (detail.getLevel() == level && object.getInteger("value") == grade) {
				money = object.getInteger("money");
			}
		}
		return money;
	}

	@Override
	public void sysDate(OrgTaskDetailEntity detail, OrgBountyEntity orgBounty, Long userId) {
		if (detail == null || orgBounty == null) {
			return;
		}
		List<SysUserEntity> selectList = userDao.selectUserOgr(detail.getLevel());
		List<YjViewEntity> yhList = new ArrayList<YjViewEntity>();
		for (SysUserEntity user : selectList) {
			if(user.getUserId() == 448){
				System.out.println("==================");
			}
			if (userId != null && !userId.equals(user.getUserId())) {
				continue;
			}
			if (user.getGwdj() != detail.getLevel()) {
				continue;
			}
			YjViewEntity yjViewEntity = new YjViewEntity();
			JSONObject jsonObject = orgBountyDao.getOne(
					DateUtil.formatDate(DateUtil.beginOfMonth(DateUtil.parse(detail.getPlanDate(), "yyyy-MM"))),
					DateUtil.formatDate(DateUtil.endOfMonth(DateUtil.parse(detail.getPlanDate(), "yyyy-MM"))),
					user.getUserId());
			if (jsonObject == null || jsonObject.getBigDecimal("nhyj") == null) {
				continue;
			}
			yjViewEntity.setBountyId(orgBounty.getId());
			yjViewEntity.setNhyj(jsonObject.getBigDecimal("nhyj"));
			yjViewEntity.setPlanDate(detail.getPlanDate());
			yjViewEntity.setZbyj(jsonObject.getBigDecimal("zbyj"));
			yjViewEntity.setUserName(user.getUsername());
			yjViewEntity.setUserId(user.getUserId());
			//获取职位业绩
			Integer taskMoney = getYeji(detail, user.getGwdj(), user.getGrade());
			Double zbyj = jsonObject.getDouble("zbyj");
			//业绩率
			double wcl = new BigDecimal(zbyj).divide(new BigDecimal(taskMoney), 4, RoundingMode.UP).doubleValue();
			BigDecimal reate = BigDecimal.ZERO;
			if (user.getGwdj() == 2) {
				if (wcl >= 0.6) {
					reate = orgBounty.getOrg2Fail();
				} else {
					reate = orgBounty.getOrg2Suc();
				}
			}
			if (user.getGwdj() == 3) {
				if (wcl >= 0.6) {
					
					reate = orgBounty.getOrg3Fail();
				} else {
					reate = orgBounty.getOrg3Suc();
				}
			}

			if (user.getGwdj() == 4) {
				if (wcl >= 0.6) {
					reate = orgBounty.getOrg4Fail();
					
				} else {
					reate = orgBounty.getOrg4Suc();
				}
			}
			yjViewEntity.setTask(new BigDecimal(taskMoney));
			yjViewEntity.setWcl(new BigDecimal(wcl));
			yjViewEntity.setJqje(jsonObject.getBigDecimal("nhyj").multiply(reate.multiply(new BigDecimal(100))));
			yjViewEntity.setJqxs(reate);
			//user.getLevel(), user.getGrade()
			yjViewEntity.setLevel(user.getGwdj() + "");
			yjViewEntity.setGrade(user.getGrade() + "");
			yjViewEntity.setUserName(user.getRealname());
			yjViewEntity.setCreateBy(detail.getCreateBy());
			yjViewEntity.setCreateDate(new Date());
			yjViewEntity.setUptBy(detail.getCreateBy());
			yjViewEntity.setUptDate(new Date());
			yhList.add(yjViewEntity);
		}
		
		this.saveBatch(yhList);
	}

	@Override
	public void sysTask(OrgTaskEntity task) {
		// TODO Auto-generated method stub
		task.getOrgTaskDetailList().stream().forEach(detail -> {
			OrgBountyEntity orgBounty = orgBountyDao.selectOne(new QueryWrapper<OrgBountyEntity>()
					.le("start_date", detail.getPlanDate()).ge("end_date", detail.getPlanDate()).last(" limit 1"));
			this.sysDate(detail, orgBounty, null);
		});
	}

	@Override
	public void yjCreate(JSONObject prams) {
		// TODO Auto-generated method stub
		this.baseMapper.delete(new QueryWrapper<YjViewEntity>().eq("plan_date",prams.getString("month")));
		List<OrgTaskDetailEntity> selectList = orgTaskDetailDao
				.selectList(new QueryWrapper<OrgTaskDetailEntity>().eq("plan_date", prams.getString("month")));
		selectList.stream().forEach(detail -> {
			OrgBountyEntity orgBounty = orgBountyDao.selectOne(new QueryWrapper<OrgBountyEntity>()
					.le("start_date", detail.getPlanDate()).ge("end_date", detail.getPlanDate()).last(" limit 1"));
			this.sysDate(detail, orgBounty, prams.getLong("userId"));
		});
	}

}