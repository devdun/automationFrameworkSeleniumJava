package utils.config;

import java.io.File;

public class CreateDirectories {

    public void createFolderWithSpecificName(String Filepath){
        File theDir = new File(Filepath);
        if (!theDir.exists()) {
            System.out.println("creating directory: " + theDir.getName());
            boolean result = false;
            try{
                theDir.mkdir();
                result = true;
            }
            catch(SecurityException se){
                //handle it
            }
            if(result) {
                System.out.println("DIR created");
            }
        }

    }
}
