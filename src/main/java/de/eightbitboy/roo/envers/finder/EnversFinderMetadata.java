package de.eightbitboy.roo.envers.finder;

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

public class EnversFinderMetadata extends AbstractItdTypeDetailsProvidingMetadataItem {
	
	private static Logger LOG = HandlerUtils.getLogger(EnversFinderMetadata.class);
	
    private static final String PROVIDES_TYPE_STRING = EnversFinderMetadata.class.getName();
    private static final String PROVIDES_TYPE = MetadataIdentificationUtils.create(PROVIDES_TYPE_STRING);
    
    private static final String ENTITY_MANAGER_METHOD_NAME = "entityManager";
    
    private EnversFinderAnnotationValues annotationValues;
    private String entityName;

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
	
	protected EnversFinderMetadata(EnversFinderAnnotationValues annotationValues, String identifier, JavaType aspectName, PhysicalTypeMetadata governorPhysicalTypeMetadata) {
		super(identifier, aspectName, governorPhysicalTypeMetadata);
		LOG.info("Adding EnversFinder code");
		
		this.annotationValues = annotationValues;
		this.entityName = annotationValues.getPath().toUpperCase();				

		builder.addMethod(getFindAllAuditsMethod());
		
		itdTypeDetails = builder.build();
	}
	
    private MethodMetadata getFindAllAuditsMethod() {
    	final JavaSymbolName methodName = new JavaSymbolName("findAllAudits");
    	
    	final InvocableMemberBodyBuilder bodyBuilder = new InvocableMemberBodyBuilder();
    	
    	bodyBuilder.appendFormalLine("return "
    		+ ENTITY_MANAGER_METHOD_NAME
            + "().createQuery(\"SELECT o FROM " + entityName + " o\", "
            + destination.getSimpleTypeName() + ".class).getResultList();");
    	
    	
    	
    	
    	
    	
    	bodyBuilder.appendFormalLine("System.out.print(\"finding all audits\");");
 
    	final MethodMetadataBuilder methodBuilder = new MethodMetadataBuilder(
    		getId(),
    		Modifier.PUBLIC,
    		methodName,
    		JavaType.VOID_PRIMITIVE,
    		bodyBuilder);
    	
    	return methodBuilder.build();
    }
}