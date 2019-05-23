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

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public ArrayList<NewsTabData> getChildren() {
            return children;
        }

        public void setChildren(ArrayList<NewsTabData> children) {
            this.children = children;
        }

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

    public class NewsTabData implements Serializable{
        public int id;
        public String title;
        public int type;
        public String url;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }


        public String toString() {
            return this.title+this.getUrl()+this.getType()+"传递成功";
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
