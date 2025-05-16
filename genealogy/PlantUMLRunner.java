package genealogy;

import java.io.*;

public class PlantUMLRunner {
    private static String plantUmlJarPath;

    public static void setJarPath(String jarPath) {
        plantUmlJarPath = jarPath;
    }

    public static void generateDiagram(String umlSource, String outputDir, String outputFileName) throws IOException, InterruptedException {
        File dir = new File(outputDir);
        if (!dir.exists()) dir.mkdirs();
        File umlFile = new File(dir, outputFileName + ".puml");
        try (FileWriter writer = new FileWriter(umlFile)) {
            writer.write(umlSource);
        }
        ProcessBuilder pb = new ProcessBuilder(
            "java", "-jar", plantUmlJarPath, umlFile.getAbsolutePath()
        );
        pb.directory(dir);
        pb.inheritIO();
        Process process = pb.start();
        process.waitFor();
    }
}
