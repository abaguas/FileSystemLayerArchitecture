package pt.tecnico.mydrive.domain;

import org.jdom2.Element;
import org.joda.time.DateTime;

import pt.tecnico.mydrive.exception.LinkWithoutContentException;
import pt.tecnico.mydrive.exception.MaximumPathException;

import org.jdom2.Document;

public class Link extends Link_Base {

    public Link(String name, int id, User owner, String content, Directory father) {
    	if(content.equals("")){
    		throw new LinkWithoutContentException(name);
    	}
    	else if(content.length()>1024){
    		throw new MaximumPathException(name);
    	}

    	init(name,id,owner,content, father);
    }
//    public Link(String name, int id, String content) {
//    }
    public Link(Element link_element, User owner, Directory father){
        xmlImport(link_element, owner, father);

    }
    
    @Override
    public void writeContent(User user, Directory directory, String content){
    	File file = directory.get(getContent());
    	file.writeContent(user, directory, content);
    }
    
    public void execute(){
    }
  
    @Override
    public String toString(){
    	String t = "Link";
    	t+=print()+"->"+getContent();
    	return t;
    }
    
    public String ls(){
		return getContent();
	}

    public void xmlImport(Element link_element, User owner, Directory father){
        int id= Integer.parseInt(link_element.getAttribute("id").getValue());
        String name = link_element.getChildText("name");
        String perm= link_element.getChildText("perm");
        String contents= link_element.getChildText("value");
        if(perm == null){
            perm = "rwxd--x-";
        }
        Permission ownpermission = new Permission(perm.substring(0,4));
        Permission otherspermission = new Permission(perm.substring(4));
        setUserPermission(ownpermission);
        setOthersPermission(otherspermission);
        init(name, id, owner, contents, father);
    }
    
    @Override
    public void xmlExport(Element element_mydrive){
        Element element = new Element ("link");
        element.setAttribute("id",Integer.toString(getId()));
        
        Element path_element = new Element ("path");
        path_element.setText(getAbsolutePath());
        element.addContent(path_element);

        Element name_element = new Element ("name");
        name_element.setText(getName());
        element.addContent(name_element);

        Element owner_element = new Element ("owner");
        owner_element.setText(getOwner().getUsername());
        element.addContent(owner_element);

        Element permission_element = new Element ("perm");
        permission_element.setText(getUserPermission().toString() + getOthersPermission().toString());
        element.addContent(permission_element);

        Element value_element = new Element ("value");
        value_element.setText(getContent());
        element.addContent(value_element);
        
        Element lastChange_element = new Element ("lastChange");
    	lastChange_element.setText(getLastChange().toString());
    	element.addContent(lastChange_element);

        element_mydrive.addContent(element);
    }
    
}