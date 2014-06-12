package de.eightbitboy.roo.envers.controller;

import org.springframework.roo.classpath.PhysicalTypeMetadata;
import org.springframework.roo.classpath.details.ClassOrInterfaceTypeDetails;
import org.springframework.roo.classpath.details.annotations.populator.AbstractAnnotationValues;
import org.springframework.roo.classpath.details.annotations.populator.AutoPopulate;
import org.springframework.roo.classpath.details.annotations.populator.AutoPopulationUtils;
import org.springframework.roo.model.JavaType;

public class EnversControllerAnnotationValues extends AbstractAnnotationValues {

	@AutoPopulate JavaType type;
	
	static final JavaType ROO_ENVERS_CONTROLLER = new JavaType("de.eightbitboy.roo.envers.controller.RooEnversController");
	
	protected EnversControllerAnnotationValues(final ClassOrInterfaceTypeDetails governorPhysicalTypeDetails) {
		super(governorPhysicalTypeDetails, ROO_ENVERS_CONTROLLER);
		AutoPopulationUtils.populate(this, annotationMetadata);
	}
	
	protected EnversControllerAnnotationValues(final PhysicalTypeMetadata governorPhysicalTypeMetadata) {
		super(governorPhysicalTypeMetadata, ROO_ENVERS_CONTROLLER);
		AutoPopulationUtils.populate(this, annotationMetadata);
	}
	
	public JavaType getType(){
		return type;
	}
}