package Package;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class name_list {

    static List filelist = new ArrayList();
    static String strPath = new String("F:\\wz");
    List addresses = new ArrayList();

    public static void main(String[] args) throws Exception {
// TODO Auto-generated method stub
        genFile(getFileList(strPath));
    }


    public static List<File> getFileList(String strPath) {
        File dir = new File(strPath);
        File[] files = dir.listFiles(); // 该文件目录下文件全部放入数组
        String regx=".*(Controller\\.java)$";
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                String fileName = files[i].getName();
                if (files[i].isDirectory()) { // 判断是文件还是文件夹
                    getFileList(files[i].getAbsolutePath()); // 获取文件绝对路径
                } else {
//                    System.out.println(regx);
//                    System.out.println(fileName);
                    Pattern compile = Pattern.compile(regx);
                    Matcher matcher = compile.matcher(fileName);
                    boolean matches = matcher.matches();
                    //1.如果文件夹 且名字等于controller
                    if (matches) {
//                        try {
//                            Thread.sleep(1000);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
                        String strFileName = files[i].getAbsolutePath();
//                        System.out.println("---" + strFileName);
                        filelist.add(files[i]);

                        FileReader fileReader=null;
                        BufferedReader bufferedReader = null;
                        String pattern="(@(Get|Post|Request)Mapping\\()(.*)(\\))";
                        Pattern r = Pattern.compile(pattern);

//                        System.out.println(pattern);

                        try {
                            //2.读取下面的文件名
                            fileReader=new FileReader(strFileName);
                            bufferedReader = new BufferedReader(fileReader);
                            String str=null;
                            while ( ( str=bufferedReader.readLine())!=null){
//                                System.out.println(str);
//                                System.out.println("--------------------------------------------------");
                                //3.读取文件获得@ConTroller后面的值
                                Matcher m = r.matcher(str);
                                if (m.find()){
                                    System.out.println(m.group(3));
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                if (bufferedReader!=null){
                                    bufferedReader.close();
                                }
                                if (fileReader!=null){
                                    fileReader.close();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }




                        //4.获得所有接口地址
                    }
                }
            }


        }
        return filelist;
    }

    public static void genFile(List<File> nameList) throws Exception {
//        File file = new File("E:/working/name_list.txt");
//        FileWriter fw = new FileWriter(file);
//        for (File file1 : nameList) {
//            String temp = file1.getName().replaceAll(".jpg", "\r\n");
//            fw.write(temp);
//            fw.flush();
//        }
//        fw.close();
    }
}
