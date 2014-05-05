package de.eightbitboy.roo.envers.controller;

import java.lang.reflect.Modifier;
import java.util.logging.Logger;

import org.springframework.roo.classpath.PhysicalTypeIdentifierNamingUtils;
import org.springframework.roo.classpath.PhysicalTypeMetadata;
import org.springframework.roo.classpath.details.MethodMetadata;
import org.springframework.roo.classpath.details.MethodMetadataBuilder;
import org.springframework.roo.classpath.itd.AbstractItdTypeDetailsProvidingMetadataItem;
import org.springframework.roo.classpath.itd.InvocableMemberBodyBuilder;
import org.springframework.roo.metadata.MetadataIdentificationUtils;
import org.springframework.roo.model.JavaSymbolName;
import org.springframework.roo.model.JavaType;
import org.springframework.roo.project.LogicalPath;
import org.springframework.roo.support.logging.HandlerUtils;

public class EnversControllerMetadata extends AbstractItdTypeDetailsProvidingMetadataItem {
	
	private static Logger LOG = HandlerUtils.getLogger(EnversControllerMetadata.class);
	
    private static final String PROVIDES_TYPE_STRING = EnversControllerMetadata.class.getName();
    private static final String PROVIDES_TYPE = MetadataIdentificationUtils.create(PROVIDES_TYPE_STRING);
    
    private EnversControllerAnnotationValues annotationValues;

    public static final String getMetadataIdentiferType() {
        return PROVIDES_TYPE;
    }
    
    public static final String createIdentifier(JavaType javaType, LogicalPath path) {
        return PhysicalTypeIdentifierNamingUtils.createIdentifier(PROVIDES_TYPE_STRING, javaType, path);
    }
    
    public static final JavaType getJavaType(String metadataIdentificationString) {
        return PhysicalTypeIdentifierNamingUtils.getJavaType(PROVIDES_TYPE_STRING, metadataIdentificationString);
    }

    public static final LogicalPath getPath(String metadataIdentificationString) {
        return PhysicalTypeIdentifierNamingUtils.getPath(PROVIDES_TYPE_STRING, metadataIdentificationString);
    }
	
	protected EnversControllerMetadata(EnversControllerAnnotationValues annotationValues, String identifier, JavaType aspectName, PhysicalTypeMetadata governorPhysicalTypeMetadata) {
		super(identifier, aspectName, governorPhysicalTypeMetadata);
		LOG.info("Adding EnversController code");
		
		this.annotationValues = annotationValues;
		//LOG.info("Path: " + this.annotationValues.getPath());
		
		builder.addMethod(getListAuditsMethod());
		
		itdTypeDetails = builder.build();
	}
	
    private MethodMetadata getListAuditsMethod() {
    	final JavaSymbolName methodName = new JavaSymbolName("listAudits");
    	
    	final InvocableMemberBodyBuilder bodyBuilder = new InvocableMemberBodyBuilder();
    	bodyBuilder.appendFormalLine("System.out.print(\"doing something\");");
    	

    	
    	final MethodMetadataBuilder methodBuilder = new MethodMetadataBuilder(
    		getId(),
    		Modifier.PUBLIC,
    		methodName,
    		JavaType.VOID_PRIMITIVE,
    		bodyBuilder);
    	
    	return methodBuilder.build();
    }
}