package de.eightbitboy.roo.envers.view;

import java.util.logging.Logger;

import org.apache.felix.scr.annotations.Reference;
import org.springframework.roo.model.JavaType;
import org.springframework.roo.process.manager.FileManager;
import org.springframework.roo.support.logging.HandlerUtils;

public class EnversViewManager {
	@Reference private FileManager fileManager;
	
	private static Logger LOG = HandlerUtils.getLogger(EnversViewManager.class);
	
	private JavaType type;
	private String typeName;
	private String typeNamePlural;
	
	public EnversViewManager(JavaType type){
		this.type = type;
		this.typeName = this.type.getSimpleTypeName();
		this.typeNamePlural = this.typeName + "s";
	}
	
	public void addViews(){
		LOG.info("Adding Envers views");
	}
	
	private void getListAuditsView(){
		
	}
}