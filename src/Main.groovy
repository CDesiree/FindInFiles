import groovy.io.FileType

import java.text.SimpleDateFormat

class FindInFiles {

  static void main(String [] args){
    String folderPath = args[0]
    String searchString = args[1]
    String replaceString = args[2]

    File folder = new File(folderPath)
    if(!folder.isDirectory()) {
      println("Invalid folder path: ${folderPath}")
      System.exit(1)
    }

    File backupDir = new File("${folderPath}/backup_${new SimpleDateFormat('yyyyMMddHHmmss').format(new Date())}")
      if(!backupDir.exists()) {
        backupDir.mkdirs()
      }

    List<Map> updateLogs = []

    folder.eachFileMatch(FileType.FILES, ~/.*\.txt/) {File file
      ->
        File backupFile = new File("${backupDir}/${file.name}")
        file.withInputStream {input ->
          backupFile.withOutputStream {output->
            output << input
            //save files to backup directory
          }
        }

        String content = file.text
        if (content.contains(searchString)) {
          String updatedContent = content.replaceAll(searchString, replaceString)
          file.text = updatedContent
        }
    }

  }
}

