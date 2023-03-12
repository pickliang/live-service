// package io.live_mall.modules.server.controller.scheduling;
//
// import java.util.Date;
// import java.util.List;
//
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.scheduling.annotation.EnableScheduling;
// import org.springframework.scheduling.annotation.Scheduled;
// import org.springframework.stereotype.Component;
//
// import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//
// import cn.hutool.core.date.DateUtil;
// import io.live_mall.modules.server.entity.ProductEntity;
// import io.live_mall.modules.server.entity.RaiseEntity;
// import io.live_mall.modules.server.service.ProductService;
// import io.live_mall.modules.server.service.RaiseService;
//
// @Component
// @EnableScheduling
// public class RaisingScheduler {
//
// 	@Autowired
// 	RaiseService raiseService;
//
//
// 	@Autowired
// 	ProductService productService;
//
// 	/**
// 		* 自动发行
// 		*/
//
// 	@Scheduled(cron = "0 5/5 * * * ? ")
// 	public void start() {
// 		List<RaiseEntity> raiselist = raiseService.list(new QueryWrapper<RaiseEntity>()
// 				.le("begin_date", DateUtil.format(new Date(), "yyy-MM-dd HH:mm:ss")).eq("status", 0));
// 		raiselist.stream().forEach(e -> {
// 			e.setStatus(1);
// 		});
// 		raiseService.updateBatchById(raiselist);
// 	}
//
// 	/**
// 	* 自动叫停
// 	*/
// 	@Scheduled(cron = "0 5/5 * * * ? ")
// 	public void end() {
// 		List<RaiseEntity> raiselist = raiseService.list(new QueryWrapper<RaiseEntity>()
// 				.le("end_date", DateUtil.format(new Date(), "yyy-MM-dd HH:mm:ss")).eq("status", 1));
// 		raiselist.stream().forEach(e -> {
// 			e.setStatus(2);
// 		});
// 		raiseService.updateBatchById(raiselist);
//
// 		List<RaiseEntity> raiselist2 = raiseService.list(new QueryWrapper<RaiseEntity>().eq("status", 1));
// 		raiselist2.stream().forEach(e -> {
// 			raiseService.down(e.getId());
// 		});
// 	}
//
// 	@Scheduled(cron = "0 5/5 * * * ? ")
// 	public void uptStatus() {
// 		List<ProductEntity> list = productService.list(new QueryWrapper<ProductEntity>().in("status", "1","2","4"));
// 		for (ProductEntity productEntity : list) {
// 			try {
// 				productService.uptStatus(productEntity.getId());
// 			} catch (Exception e) {
// 				// TODO: handle exception
// 				e.printStackTrace();
// 			}
//
// 		}
// 	}
//
// }
