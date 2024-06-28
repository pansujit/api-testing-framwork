package com.glide.qa.backend.common.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.io.FileUtils;

/**
 * This class contains all logic about file download.
 *
 * @author sujitpandey
 *
 */
public class FileDownloader {

  private FileDownloader() {

  }
  private static final String FILEPATH = "target/newFolder/";

  /**
   * This method will download the file with given filename.
   *
   * @param url : Should be String
   * @param filename : Should be String
   */
  public static void filedownloader(String url, String filename) {
    try {
      FileUtils.copyURLToFile(new URL(url), new File(FILEPATH + filename));
    } catch (IOException e) {

      e.printStackTrace();
    }
  }

}
