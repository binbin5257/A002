package cn.lds.chatcore.imtp;

import android.os.Binder;

/**
 * 
 */
public class ImtpBinder extends Binder{

	ImtpService imtpService = null;
	
	public ImtpBinder(ImtpService service){
		imtpService = service;
	}
	
	public ImtpService getService(){
		return imtpService;
	}
}
