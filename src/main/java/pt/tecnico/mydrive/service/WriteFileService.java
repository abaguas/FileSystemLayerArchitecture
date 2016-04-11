package pt.tecnico.mydrive.service;

import pt.tecnico.mydrive.domain.Directory;
import pt.tecnico.mydrive.domain.File;
import pt.tecnico.mydrive.domain.MyDrive;
import pt.tecnico.mydrive.domain.User;
import pt.tecnico.mydrive.exception.PermissionDeniedException;
import pt.tecnico.mydrive.exception.NoSuchFileException;
import pt.tecnico.mydrive.exception.FileIsNotWriteAbleException;



public class WriteFileService extends MyDriveService
{
    private String fileName;
    private String content;
    private long token;

    public WriteFileService(String fileName, String content, long token)
    {        
        this.fileName=fileName;
        this.content=content;
        this.token=token;
    }


    
    public final void dispatch() throws PermissionDeniedException, NoSuchFileException, FileIsNotWriteAbleException {
       MyDrive md = getMyDrive();
       User currentUser = md.getSessionByToken(token).getCurrentUser();
       Directory currentDir = md.getSessionByToken(token).getCurrentDir();
       
       
       File file = currentDir.get(fileName); // throws no such file exception            
       
       boolean ownerPermission = file.getOwner().getUsername().equals(currentUser.getUsername()) || currentUser.getUsername().equals("root");
       boolean writePermission = file.getOthersPermission().getWrite() && currentUser.getOthersPermission().getWrite();
       
       if(!(ownerPermission || writePermission)){
       		throw new PermissionDeniedException("Writing on " + fileName);
       }
       
       md.writeable(file); //should it be mydrive?
//       currentDir.writeFile())   
    }
    
}
