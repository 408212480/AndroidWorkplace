package qunincey.com.smartcity.domain;

import java.io.Serializable;
import java.util.ArrayList;

public class NewsMenu implements  Serializable{

    public int retcode;

    public ArrayList<Integer> extend;

    public ArrayList<NewsMenuData> data;

    public ArrayList<NewsMenuData> getData() {
        return data;
    }

    public void setData(ArrayList<NewsMenuData> data) {
        this.data = data;
    }

    public class NewsMenuData implements Serializable {
        public int id;
        public String title;
        public int type;

        public ArrayList<NewsTabData> children;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        @Override
        public String toString() {
            return this.id+this.title;
        }
    }

    public class NewsTabData{
        public int id;
        public String title;
        public int type;
        public String url;


        @Override
        public String toString() {
            return this.title;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }


    @Override
    public String toString() {
        return super.toString();
    }
}
