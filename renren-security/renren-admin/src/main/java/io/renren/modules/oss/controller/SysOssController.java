/**
 * Copyright (c) 2018 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package io.renren.modules.oss.controller;

import com.google.gson.Gson;
import io.renren.common.annotation.LogOperation;
import io.renren.common.constant.Constant;
import io.renren.common.page.PageData;
import io.renren.common.utils.*;
import io.renren.common.validator.ValidatorUtils;
import io.renren.common.validator.group.AliyunGroup;
import io.renren.common.validator.group.QcloudGroup;
import io.renren.common.validator.group.QiniuGroup;
import io.renren.modules.oss.cloud.CloudStorageConfig;
import io.renren.modules.oss.cloud.MinioCloudStorageService;
import io.renren.modules.oss.entity.SysOssEntity;
import io.renren.modules.oss.service.SysOssService;
import io.renren.modules.sys.service.SysParamsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Arrays;
import java.util.Map;

/**
 * 文件上传
 * 
 * @author Mark sunlightcs@gmail.com
 */
@RestController
@RequestMapping("sys/oss")
@Api(tags="文件上传")
public class SysOssController {
	@Autowired
	private SysOssService sysOssService;
    @Autowired
    private SysParamsService sysParamsService;

	@Autowired
	private MinioCloudStorageService minioCloudStorageService;

    private final static String KEY = Constant.CLOUD_STORAGE_CONFIG_KEY;

	@GetMapping("page")
	@ApiOperation(value = "分页")
	@RequiresPermissions("sys:oss:all")
	public Result<PageData<SysOssEntity>> page(@ApiIgnore @RequestParam Map<String, Object> params){
		PageData<SysOssEntity> page = sysOssService.page(params);
		return new Result<PageData<SysOssEntity>>().ok(page);
	}

    @GetMapping("info")
	@ApiOperation(value = "云存储配置信息")
    @RequiresPermissions("sys:oss:all")
    public Result<CloudStorageConfig> info(){
        CloudStorageConfig config = sysParamsService.getValueObject(KEY, CloudStorageConfig.class);

        return new Result<CloudStorageConfig>().ok(config);
    }

	@PostMapping
	@ApiOperation(value = "保存云存储配置信息")
	@LogOperation("保存云存储配置信息")
	@RequiresPermissions("sys:oss:all")
	public Result saveConfig(@RequestBody CloudStorageConfig config){
		//校验类型
		ValidatorUtils.validateEntity(config);

		if(config.getType() == Constant.CloudService.QINIU.getValue()){
			//校验七牛数据
			ValidatorUtils.validateEntity(config, QiniuGroup.class);
		}else if(config.getType() == Constant.CloudService.ALIYUN.getValue()){
			//校验阿里云数据
			ValidatorUtils.validateEntity(config, AliyunGroup.class);
		}else if(config.getType() == Constant.CloudService.QCLOUD.getValue()){
			//校验腾讯云数据
			ValidatorUtils.validateEntity(config, QcloudGroup.class);
		}


		sysParamsService.updateValueByCode(KEY, new Gson().toJson(config));

		return new Result();
	}

	@PostMapping("upload")
	@ApiOperation(value = "上传文件")
	public Result<String> upload(@RequestParam("file") MultipartFile file) throws Exception {
		String fileName = minioCloudStorageService.upload(file);
		return new Result<String>().ok(fileName);
	}

	@DeleteMapping
	@ApiOperation(value = "删除")
	@LogOperation("删除")
	@RequiresPermissions("sys:oss:all")
	public Result delete(@RequestBody Long[] ids){
		sysOssService.deleteBatchIds(Arrays.asList(ids));

		return new Result();
	}

}