package qunincey.com.smartcity.domain;

import java.util.ArrayList;


public class NewsTabBean {

	public NewsTab data;

	public class NewsTab {
		public String more;
		public ArrayList<NewsData> news;
		public ArrayList<TopNews> topnews;

		public String getMore() {
			return more;
		}

		public void setMore(String more) {
			this.more = more;
		}

		public ArrayList<NewsData> getNews() {
			return news;
		}

		public void setNews(ArrayList<NewsData> news) {
			this.news = news;
		}

		public ArrayList<TopNews> getTopnews() {
			return topnews;
		}

		public void setTopnews(ArrayList<TopNews> topnews) {
			this.topnews = topnews;
		}
	}

	// 新闻列表对象
	public class NewsData {
		public int id;
		public String listimage;
		public String pubdate;
		public String title;
		public String type;
		public String url;
	}

	// 头条新闻
	public class TopNews {
		public int id;
		public String topimage;
		public String pubdate;
		public String title;
		public String type;
		public String url;
	}

	public NewsTab getData() {
		return data;
	}

	public void setData(NewsTab data) {
		this.data = data;
	}
}
