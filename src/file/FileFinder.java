package file;

import java.io.File;

/**
 * @author Gökçe Uludoğan
 * 
 */
public class FileFinder {

    /**
     * Description of FileFinder class:<br> This class is used to search a given
     * location (path), to find all files that
     * exist there and should be later shared. In this implementation, only
     * files in the specified folder are returned, not files that exist in
     * folders in the given path. For example, if "C:/Shared Folder/" is the
     * given path then only files in this folder will be shared, not files in
     * upper or lower depth of the given location.
     */
    public FileFinder() {
    }

    /**
     * @param location The location in which files will be searched from.
     * @return SharedFile[] - A list of SharedFiles made by the files that
     * existed in the given directory.
     */
    public static SharedFile[] findNow(String location) {
        //"fold" is the folder in which are the files to be shared:
        
        File fold = new File(location);
        //System.out.println("location:"+location );
//Get the files and folders in "fold" and put them in a list 
        //System.out.println("len"+fold.listFiles().length);
        File[] listOfFiles = fold.listFiles();
                       
        //Each client can share up to 99 files.
        File[] listOfFiles2 = new File[99];

        int count=0;
        for (int i = 0; i < listOfFiles.length; i++) {
            //From files and folders choose only files:
            if (listOfFiles[i].isFile()) {
                count++;
                //System.out.println(listOfFiles[i]);
                //And put them in a second list:
                listOfFiles2[i] = listOfFiles[i];
            }
        }
        
        // Now from the second list we create ShareFile objects, that represent
        // real file objects in code-level representations.

        SharedFile[] sharedFiles = new SharedFile[count];
        for (int y = 0; y < count; y++) {
            String[] splittedText = listOfFiles2[y].getName().split("\\.", 2);
            System.out.println(splittedText[0]+" "+splittedText[1]);
            long size = listOfFiles2[y].length();

            SharedFile sharedFile = new SharedFile(0, splittedText[0], splittedText[1], size, "");
            sharedFiles[y] = sharedFile;
        }
        //System.out.println("return");
        //Return the second list:
        return sharedFiles;
    }
}