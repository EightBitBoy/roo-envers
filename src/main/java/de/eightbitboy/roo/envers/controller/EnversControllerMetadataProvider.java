package de.eightbitboy.roo.envers.controller;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.osgi.service.component.ComponentContext;
import org.springframework.roo.addon.web.mvc.controller.details.WebMetadataService;
import org.springframework.roo.classpath.PhysicalTypeIdentifier;
import org.springframework.roo.classpath.PhysicalTypeMetadata;
import org.springframework.roo.classpath.itd.AbstractItdMetadataProvider;
import org.springframework.roo.classpath.itd.ItdTypeDetailsProvidingMetadataItem;
import org.springframework.roo.model.JavaType;
import org.springframework.roo.project.LogicalPath;

@Component
@Service
public class EnversControllerMetadataProvider extends AbstractItdMetadataProvider {
	@Reference WebMetadataService webMetadataService;

    protected void activate(ComponentContext context) {
        metadataDependencyRegistry.registerDependency(PhysicalTypeIdentifier.getMetadataIdentiferType(), getProvidesType());
        addMetadataTrigger(new JavaType(RooEnversController.class.getName()));
    }
	
    protected void deactivate(ComponentContext context) {
        metadataDependencyRegistry.deregisterDependency(PhysicalTypeIdentifier.getMetadataIdentiferType(), getProvidesType());
        removeMetadataTrigger(new JavaType(RooEnversController.class.getName()));    
    }
	
    protected ItdTypeDetailsProvidingMetadataItem getMetadata(
    		String metadataIdentificationString,
    		JavaType aspectName,
    		PhysicalTypeMetadata governorPhysicalTypeMetadata,
    		String itdFilename) {
        // Pass dependencies required by the metadata in through its constructor
    	final EnversControllerAnnotationValues annotationValues = new EnversControllerAnnotationValues(governorPhysicalTypeMetadata);
    	
        return new EnversControllerMetadata(annotationValues, metadataIdentificationString, aspectName, governorPhysicalTypeMetadata);
    }
	
    public String getItdUniquenessFilenameSuffix() {
        return "EnversController";
    }
	
	
    protected String getGovernorPhysicalTypeIdentifier(String metadataIdentificationString) {
        JavaType javaType = EnversControllerMetadata.getJavaType(metadataIdentificationString);
        LogicalPath path = EnversControllerMetadata.getPath(metadataIdentificationString);
        return PhysicalTypeIdentifier.createIdentifier(javaType, path);
    }

    protected String createLocalIdentifier(JavaType javaType, LogicalPath path) {
        return EnversControllerMetadata.createIdentifier(javaType, path);
    }

    public String getProvidesType() {
        return EnversControllerMetadata.getMetadataIdentiferType();
    }
}