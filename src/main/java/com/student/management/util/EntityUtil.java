package com.student.management.util;

import java.util.Date;

import com.student.management.dto.MessageDto;
import com.student.management.entity.ObjectUtil;



public class EntityUtil {

	static Date date = new Date();
	public static void setEntityDetails(ObjectUtil objectUtils, String name, String user) {
		objectUtils.setName(name);
		objectUtils.setCreatedBy(user);
		objectUtils.setModifiedBy(user);
		objectUtils.setCreatedDate(date);
		objectUtils.setModifiedDate(date);
		objectUtils.setStatus(ConstantUtil.STATUS_ACTIVE);
	}
	
	public static void setMessageDto(MessageDto messageDto, String status, String msg) {
		messageDto.setStatus(status);
		messageDto.setStatusMessage(msg);
	}
}
