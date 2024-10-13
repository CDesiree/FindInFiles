import groovy.io.FileType

import java.text.SimpleDateFormat

class FindInFiles {

  static void main(String [] args){
    String folderPath = args[0]
    String searchString = args[1]
    String replaceString = args[2]
    String outputDir = args.length > 3 ? args[3] : null

    File folder = new File(folderPath)
    validateDirectory(folder, folderPath)

    File backupDir = createBackupDir(folderPath)

    List<Map> updateLogs = []

    folder.eachFileRecurse(FileType.FILES) {File file
      ->
        backupFile(backupDir, file)
        String content = file.text
        if (content.contains(searchString)) {
          file.text = content.replaceAll(searchString, replaceString)
          updateLogs << createLogEntry(file.name, searchString)
        }
    }

    if (outputDir != null) {
      File logFile = new File("${outputDir}/update_log_${new SimpleDateFormat('yyyyMMddHHmmss').format(new Date())}.txt")
      if (!logFile.exists()) {
        logFile.createNewFile()
      }
      logFile.withWriter {writer ->
        writer.write(String.format("%-30s %-20s %-30s %30s%n", "File name", "Pattern Found", "Start time", "End time"))
        writer.write(String.format("%-30s %-20s %-30s %30s%n", "-----------", "------------", "----------", "--------"))
        updateLogs.each {log ->
          writer.write(String.format("%-30s %-20s %-30s %30s%n",
                  log.fileName,
                  log.patternFound,
                  log.startTime,
                  log.endTime))
        }
      }
    }

    println("Process completed. ${updateLogs.size()} files updated.")
    println("Backup folder: ${backupDir}")

  }

  private static void backupFile(File backupDir, File file) {
    File backupFile = new File("${backupDir}/${file.name}")
    file.withInputStream { input ->
      backupFile.withOutputStream { output ->
        output << input
        //save files to backup directory
      }
    }
  }

  private static File createBackupDir(String folderPath) {
    File backupDir = new File("${folderPath}/backup_${new SimpleDateFormat('yyyyMMddHHmmss').format(new Date())}")
    if (!backupDir.exists()) {
      backupDir.mkdirs()
    }
    return backupDir
  }

  private static void validateDirectory(File folder, String folderPath) {
    if (!folder.isDirectory()) {
      println("Invalid folder path: ${folderPath}")
      System.exit(1)
    }
  }

  private static Map createLogEntry(String fileName, String patterFound) {
    return [
            fileName: fileName,
            patternFound: patterFound,
            startTime: new Date().toString(),
            endTime: new Date().toString()
    ]
  }
}

