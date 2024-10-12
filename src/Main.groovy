class FindInFiles {

  static void main(String [] args){
    String folderPath = args[0]

    File folder = new File(folderPath)
    if(!folder.isDirectory()) {
      println("Invalid folder path: ${folderPath}")

    }else{
      println("Valid folder path thanks~")
      System.exit(1)
    }
  }
}

