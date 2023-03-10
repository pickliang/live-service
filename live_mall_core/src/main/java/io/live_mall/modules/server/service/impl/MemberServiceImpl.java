package io.live_mall.modules.server.service.impl;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.live_mall.common.utils.DateUtils;
import io.live_mall.common.utils.PageUtils;
import io.live_mall.common.utils.Query;
import io.live_mall.constants.MmsConstants;
import io.live_mall.modules.server.dao.*;
import io.live_mall.modules.server.entity.*;
import io.live_mall.modules.server.service.MemberService;
import io.live_mall.modules.server.service.MmsMemberService;
import io.live_mall.modules.server.service.OrderService;
import io.live_mall.modules.sys.service.SysUserService;
import io.live_mall.sms.mms.MmsClient;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service("memberService")
@AllArgsConstructor
public class MemberServiceImpl extends ServiceImpl<MemberDao, MemberEntity> implements MemberService {

	private final OrderService orderService;
	private final SysUserService  userService;
	private final CustomerUserDao customerUserDao;
	private final MmsTemplateDao mmsTemplateDao;
	private final MmsSmsContentDao mmsSmsContentDao;
	
	

	@Override
	public PageUtils queryPage(JSONObject params) {

		QueryWrapper<MemberEntity> queryWrapper = new QueryWrapper<MemberEntity>();

//		if (StringUtils.isNotBlank(params.getString("saleId"))) {
//			queryWrapper.eq("sale_id", params.getString("saleId"));
//		}
//
//		if (StringUtils.isNotBlank(params.getString("sex"))) {
//			queryWrapper.eq("sex", params.getString("sex"));
//		}
//
		/**
		 * ??????custName???p_member???????????????????????????????????????????????????
		 */
		if (StringUtils.isNotBlank(params.getString("custName"))){
			queryWrapper.like("cust_name",params.getString("custName"));
		}

		/**
		 * realname??????p_member??????????????????????????????????????????????????????
		 */
//		if (StringUtils.isNotBlank(params.getString("salename"))){
//			queryWrapper.like("salename",params.getString("salename"));
//		}
		/**
		 * ??????????????????????????????????????????????????????????????????????????????Entity?????????????????????????????????realname
		 */

		IPage<MemberEntity> page = this.page(new Query<MemberEntity>().getPage(params),
				queryWrapper.orderByDesc("create_date"));
		IPage<MemberEntity> page1;
		page.getRecords().stream().forEach(e -> {
			//??????????????????
			OrderEntity orderEntity = orderService.getOne(new QueryWrapper<OrderEntity>().eq("cust_id", e.getMemberNo())
					.orderByDesc("create_date").last(" limit 1 "));
			if (orderEntity != null) {
				e.setLastDate(DateUtil.formatDate(orderEntity.getCreateDate()));
			}
			//??????
			e.setTotalMoeny(orderService.sumMoenyMember(e.getMemberNo()));


			if(e.getSaleId() !=null) {
				e.setSalesUser(userService.getById(e.getSaleId()));
			}

			if (StringUtils.isNotBlank(e.getCardNum())) {
				// ????????????????????????
				Integer count = customerUserDao.selectCount(Wrappers.lambdaQuery(CustomerUserEntity.class)
						.eq(CustomerUserEntity::getCardNum, e.getCardNum()).eq(CustomerUserEntity::getDelFlag, 0).last("LIMIT 1"));
				e.setIsLoginApplet(count > 0);
			}

		});


		return new PageUtils(page);
	}

	@Override
	public MemberEntity saveOrUpdate(OrderEntity order) {
		// TODO Auto-generated method stub
		MemberEntity member = this.getOne(new QueryWrapper<MemberEntity>().eq("card_num", order.getCardNum()));
		if (member == null) {
			member = new MemberEntity();
		}
		if (order.getCardNum() != null && order.getSaleId() != null) {
			member.setCardNum(order.getCardNum());
			member.setCardType(order.getCardType());
			member.setCardPhotoL(order.getCardPhotoL());
			member.setCardPhotoR(order.getCardPhotoR());
			member.setCardTime(order.getCardTime());
			member.setCardType(order.getCardType());
			member.setCustName(order.getCustomerName());
			member.setSaleId(order.getSaleId() + "");
			System.out.println("lyg==="+order.getSaleUser().getRealname());
//			member.setSalename(order.getSaleUser().getRealname());
			member.setBankCardBack(order.getBankCardBack());
			member.setBankCardFront(order.getBankCardFront());
			member.setBankNo(order.getBankNo());
			member.setBranch(order.getBranch());
			member.setOpenBank(order.getOpenBank());
			member.setEmail(order.getEmail());
			member.setAssetsDetail(order.getAssetsDetail());
			member.setPhone(order.getPhone());
			member.setSex("");
			member.setAssetsPro(order.getAssetsPro());
			this.saveOrUpdate(member);
		}
		getMemberType(member);
		return member;
	}

	@Override
	public void getMemberType(MemberEntity member) {
		try {
			//payDate
			String memberType = "A?????????????????????";
			List<OrderEntity> list = orderService
					.list(new QueryWrapper<OrderEntity>().eq("cust_id", member.getMemberNo()));
			if (list.size() > 1) {
				String payDate = list.get(0).getPayDate();
				for (OrderEntity orderEntity : list) {
					if (list.get(0).getId() != orderEntity.getId() && StringUtils.isNotBlank(payDate)
							&& payDate.equals(orderEntity.getPayDate())) {
						memberType = "B?????????????????????";
					} else {
						memberType = "C?????????";
						break;
					}
				}
			}
			member.setMemberType(memberType);
			this.updateById(member);
		} catch (Exception e) {
			// TODO: handle exception
			log.info("????????????????????????",e);
		}
	}

	@Override
	public JSONObject getTj(String custId,String salesId) {
		// TODO Auto-generated method stub
		return this.baseMapper.getTj(custId,salesId);
	}

	@Override
	public List<JSONObject> memberList(String name) {
		return this.baseMapper.memberList(name);
	}

	@Override
	public List<JSONObject> memberList(String startDate, String endDate) {
		return this.baseMapper.members(startDate, endDate);
	}

	@Autowired
	private MmsLogDao mmsLogDao;
	@Autowired
	private MmsMemberService mmsMemberService;

	@Override
	public void sendMmsIntegral(String startDate, String endDate, String memberNos, Long userId, String mmsToken) {
		List<String> ids = Arrays.asList(memberNos.split(","));
		List<JSONObject> list = this.baseMapper.mmsMemberIntegralData(ids);

		// sendMemberIntegral(mmsToken, list, logEntity.getId(), userId);
		CompletableFuture.supplyAsync(() -> sendMemberIntegral(mmsToken, list, userId, startDate, endDate));
	}

	@Transactional(rollbackFor = Exception.class)
	public boolean sendMemberIntegral(String mmsToken, List<JSONObject> list, Long userId, String startDate, String endDate) {
		// ??????mms??????????????????
		MmsLogEntity logEntity = new MmsLogEntity();
		logEntity.setStartDate(DateUtils.stringToDate(startDate, DateUtils.DATE_PATTERN));
		logEntity.setEndDate(DateUtils.stringToDate(endDate, DateUtils.DATE_PATTERN));
		logEntity.setRowNum(list.size());
		logEntity.setType(3);
		logEntity.setCreateTime(new Date());
		logEntity.setCreateUser(userId);
		mmsLogDao.insert(logEntity);
		MmsTemplateEntity mmsTemplateEntity = mmsTemplateDao.selectOne(Wrappers.lambdaQuery(MmsTemplateEntity.class)
				.eq(MmsTemplateEntity::getType, 5).orderByDesc(MmsTemplateEntity::getCreateTime).last("LIMIT 1"));
		if (Objects.nonNull(mmsTemplateEntity)) {
			// ????????????|??????
			String text = "Text1|Text2";
			List<MmsMemberEntity> entities = new ArrayList<>();
			list.forEach(record -> {
				String phone = record.getString("phone");
				String custName = record.getString("cust_name");
				StringBuilder sb = new StringBuilder();
				sb.append(custName).append("|").append(LocalDate.now().getMonthValue());
				try {
					JSONObject result = MmsClient.send(mmsToken, text, phone, sb.toString(), mmsTemplateEntity.getMmsId());
					MmsMemberEntity entity = new MmsMemberEntity();
					entity.setMmsLogId(logEntity.getId());
					entity.setMemberNo(record.getString("member_no"));
					entity.setCustomerName(custName);
					entity.setBirthday(record.getString("birthday"));
					entity.setPhone(phone);
					entity.setSaleId(record.getLong("sale_id"));
					entity.setRealname(record.getString("realname"));
					entity.setCreateTime(new Date());
					entity.setCreateUser(userId);
					if (null != result) {
						JSONObject content = result.getJSONObject("content");
						entity.setCode(Integer.valueOf(content.getString("code")));
						entity.setResult(result.toJSONString());
					}
					entities.add(entity);
					//??????????????????
					MmsSmsContentEntity mmsSmsContentEntity = new MmsSmsContentEntity();
					mmsSmsContentEntity.setTitle("??????????????????");
					mmsSmsContentEntity.setType(3);
					mmsSmsContentEntity.setReceiveId(record.getString("member_no"));
					mmsSmsContentEntity.setPhone(phone);
					String content = MmsConstants.INTEGRAL_MMS_CONTENT.replace("${Text1}", custName)
							.replace("${Text2}", String.valueOf(LocalDate.now().getMonthValue()));
					mmsSmsContentEntity.setContent(content);
					mmsSmsContentEntity.setCreateTime(new Date());
					mmsSmsContentEntity.setCreateUser(userId);
					mmsSmsContentDao.insert(mmsSmsContentEntity);
				} catch (Exception e) {
					log.error("e-->{}", e);
				}

			});
			if (!entities.isEmpty()) {
				mmsMemberService.saveBatch(entities);
			}
		}

		return true;
	}
}