package de.eightbitboy.roo.envers.view;

import java.util.logging.Logger;

import org.apache.felix.scr.annotations.Reference;
import org.springframework.roo.metadata.MetadataItem;
import org.springframework.roo.metadata.MetadataNotificationListener;
import org.springframework.roo.metadata.MetadataProvider;
import org.springframework.roo.model.JavaType;
import org.springframework.roo.process.manager.FileManager;
import org.springframework.roo.project.LogicalPath;
import org.springframework.roo.project.Path;
import org.springframework.roo.project.PathResolver;
import org.springframework.roo.project.ProjectOperations;
import org.springframework.roo.support.logging.HandlerUtils;

public class EnversViewManager implements MetadataProvider,
MetadataNotificationListener {
	@Reference private FileManager fileManager;
	@Reference private ProjectOperations projectOperations;
	
	private static final String WEB_INF_VIEWS = "/WEB-INF/views/";
	
	private static Logger LOG = HandlerUtils.getLogger(EnversViewManager.class);
	
	private JavaType type;
	private String typeName;
	private String typeNamePlural;
	private String targetPath;
	
	public EnversViewManager(JavaType type, String targetPath){
		this.type = type;
		this.typeName = this.type.getSimpleTypeName();
		this.typeNamePlural = this.typeName + "s";
		this.targetPath = targetPath + this.typeNamePlural.toLowerCase();
	}
	
	public void addViews(){
		LOG.info("Adding Envers views");
		
		/*
		final PathResolver pathResolver = this.projectOperations.getPathResolver();
        final LogicalPath webappPath = LogicalPath.getInstance(Path.SRC_MAIN_WEBAPP, null);
        String destinationPath = pathResolver.getFocusedIdentifier(Path.SRC_MAIN_WEBAPP, "/WEB-INF/views/" + this.typeNamePlural.toLowerCase());
        */
        LOG.info(this.targetPath);
	}
	
	private void getListAuditsView(){
		
	}

	@Override
	public void notify(String upstreamDependency, String downstreamDependency) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public MetadataItem get(String metadataIdentificationString) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getProvidesType() {
		// TODO Auto-generated method stub
		return null;
	}
}