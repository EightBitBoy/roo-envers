package de.eightbitboy.roo.envers.view;

import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;

import org.apache.felix.scr.annotations.Reference;
import org.springframework.roo.model.JavaType;
import org.springframework.roo.process.manager.FileManager;
import org.springframework.roo.project.ProjectOperations;
import org.springframework.roo.support.logging.HandlerUtils;
import org.springframework.roo.support.util.XmlElementBuilder;
import org.springframework.roo.support.util.XmlRoundTripUtils;
import org.springframework.roo.support.util.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import de.eightbitboy.roo.envers.util.XmlRoundTripFileManager;

public class EnversViewManager {
	@Reference private FileManager fileManager;
	@Reference private ProjectOperations projectOperations;
	@Reference private XmlRoundTripFileManager xmlRoundTripFileManager;
	
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
		
		final String listAuditsViewPath = this.targetPath + "/listaudits.jspx";
		
		/*
		Document foo = this.getListAuditsView();
		LOG.info(foo.toString());
		*/
		
		//TODO, WHY IS DOCUMENT NULL???????????????
		
		/*
		final DocumentBuilder builder = XmlUtils.getDocumentBuilder();
        final Document document = builder.newDocument();
        
        LOG.info("LOL " + document.toString());
		
        xmlRoundTripFileManager.writeToDiskIfNecessary(listAuditsViewPath, this.getListAuditsView());
        */
	}
	
	private Document getListAuditsView(){
		final DocumentBuilder builder = XmlUtils.getDocumentBuilder();
        final Document document = builder.newDocument();

        // Add document namespaces
        final Element div = new XmlElementBuilder("div", document)
                .addAttribute("xmlns:page", "urn:jsptagdir:/WEB-INF/tags/form")
                .addAttribute("xmlns:table",
                        "urn:jsptagdir:/WEB-INF/tags/form/fields")
                .addAttribute("xmlns:jsp", "http://java.sun.com/JSP/Page")
                .addAttribute("version", "2.0")
                .addChild(
                        new XmlElementBuilder("jsp:directive.page", document)
                                .addAttribute("contentType",
                                        "text/html;charset=UTF-8").build())
                .addChild(
                        new XmlElementBuilder("jsp:output", document)
                                .addAttribute("omit-xml-declaration", "yes")
                                .build()).build();
        document.appendChild(div);

        final Element fieldTable = new XmlElementBuilder("table:table",
                document)
                .addAttribute(
                        "id",
                        XmlUtils.convertId("l:" + this.type.getFullyQualifiedTypeName()))
                .addAttribute(
                        "data",
                        "${}")
                .addAttribute("path", "foooo").build();
        
        final Element pageList = new XmlElementBuilder("page:list", document)
        .addAttribute(
		                "id",
		                XmlUtils.convertId("pl:"
		                        + this.type.getFullyQualifiedTypeName()))
		        .addAttribute(
		                "items",
		                "${"
		                        + this.typeNamePlural
		                                .toLowerCase() + "}")
		        .addChild(fieldTable).build();
		pageList.setAttribute("z",
		        XmlRoundTripUtils.calculateUniqueKeyFor(pageList));
		div.appendChild(pageList);

        return document;
	}
}