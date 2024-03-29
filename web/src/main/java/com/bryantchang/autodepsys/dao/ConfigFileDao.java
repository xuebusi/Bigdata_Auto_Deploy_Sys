package com.bryantchang.autodepsys.dao;

import com.bryantchang.autodepsys.bean.HadoopNode;
import com.bryantchang.autodepsys.bean.HadoopSettings;
import com.bryantchang.autodepsys.bean.SparkNode;
import com.bryantchang.autodepsys.bean.SparkSettings;
import com.bryantchang.autodepsys.constant.Constants;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by bryantchang on 2016/12/8.
 */
@Repository
public class ConfigFileDao {
    @Resource
    HadoopNodesDao hadoopNodesDao;
    @Resource
    SparkNodeDao sparkNodeDao;
    @Resource
    SparkSettingsDao sparkSettingDao;
    @Resource
    HadoopSettingsDao hadoopSettingDao;


    public Long generateHadoopSlaveFile(String filePath) {
        Long res = Constants.SUCC;
        ArrayList<HadoopNode> datanodeList = hadoopNodesDao.getHadoopNodeListByRole("datanode");
        BufferedWriter writer = null;
        try{
            writer = new BufferedWriter(new FileWriter(new File(filePath)));
            for (int i = 0; i < datanodeList.size(); i++) {
                String slave = "";
                slave = datanodeList.get(i).getHostname();
                writer.write(slave);
                writer.newLine();
            }
        }catch (Exception e) {
            return Constants.IOERROR;
        }finally {
            try {
                writer.close();
            } catch (IOException e) {
                return Constants.IOERROR;
            }
        }
        return res;
    }



    public Long generateSparkSlaveFile(String filePath) {
        Long res = Constants.SUCC;
        ArrayList<SparkNode> workerList = sparkNodeDao.getSparkNodeByRole("slave");
        BufferedWriter writer = null;
        try{
            writer = new BufferedWriter(new FileWriter(new File(filePath)));
            for (int i = 0; i < workerList.size(); i++) {
                String slave = "";
                slave = workerList.get(i).getHostname();
                writer.write(slave);
                writer.newLine();
            }
        }catch (Exception e) {
            return Constants.IOERROR;
        }finally {
            try {
                writer.close();
            } catch (IOException e) {
                return Constants.IOERROR;
            }
        }
        return res;
    }

    public Long generateSparkDefaultFile(String filePath) {
        Long res = Constants.SUCC;
        ArrayList<SparkSettings> settings = sparkSettingDao.getAllSparkSettings();
        BufferedWriter writer = null;
        try{
            writer = new BufferedWriter((new FileWriter(filePath)));
            writer.write("###This is the spark-defaults.conf");
            writer.newLine();
            for (int i = 0; i < settings.size(); i++) {
                if(settings.get(i).getValue().equals("$")) {
                    continue;
                }
                String line = "";
                line += settings.get(i).getName() + "\t" + settings.get(i).getValue();
                writer.write(line);
                writer.newLine();
            }
        }catch(Exception e){
            return Constants.IOERROR;
        }finally {
            try {
                writer.close();
            } catch (IOException e) {
                return Constants.IOERROR;
            }
        }
        return res;
    }



    public Long generateHadoopConfFile(String filePath, ArrayList<HadoopSettings> settings) {
        Long res = Constants.SUCC;
        BufferedWriter writer = null;
        try{
            writer = new BufferedWriter((new FileWriter(filePath)));
            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            writer.write("<?xml-stylesheet type=\"text/xsl\" href=\"configuration.xsl\"?>\n");
            writer.write("<!--\n" +
                    "  Licensed under the Apache License, Version 2.0 (the \"License\");\n" +
                    "  you may not use this file except in compliance with the License.\n" +
                    "  You may obtain a copy of the License at\n" +
                    "\n" +
                    "    http://www.apache.org/licenses/LICENSE-2.0\n" +
                    "\n" +
                    "  Unless required by applicable law or agreed to in writing, software\n" +
                    "  distributed under the License is distributed on an \"AS IS\" BASIS,\n" +
                    "  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n" +
                    "  See the License for the specific language governing permissions and\n" +
                    "  limitations under the License. See accompanying LICENSE file.\n" +
                    "-->\n");
            writer.write("<configuration>\n");
            for (int i = 0; i < settings.size(); i++) {
                if(!settings.get(i).getValue().equals("") && !settings.get(i).getValue().equals("$")) {
                    writer.write("\t<property>\n");
                    writer.write("\t\t<name>" + settings.get(i).getName() + "</name>\n");
                    writer.write("\t\t<value>" + settings.get(i).getValue() + "</value>\n");
                    if (!settings.get(i).getDescription().equals("")) {
                        writer.write("\t\t<description>" + settings.get(i).getDescription() + "</description>\n");
                    }
                    writer.write("\t</property>\n");
                }
            }
            writer.write("</configuration>\n");
        }catch (Exception e) {
            return Constants.IOERROR;
        }finally {
            try {
                writer.close();
            } catch (IOException e) {
                return Constants.IOERROR;
            }
        }
        return res;
    }








}
























