import java.text.SimpleDateFormat

class FindInFiles {

  static void main(String [] args){
    String folderPath = args[0]

    File folder = new File(folderPath)
    if(!folder.isDirectory()) {
      println("Invalid folder path: ${folderPath}")
      System.exit(1)
    }

    File backupDir = new File("${folderPath}/backup_${new SimpleDateFormat('yyyyMMddHHmmss').format(new Date())}")
      if(!backupDir.exists()) {
        backupDir.mkdirs()
      }

  }
}

