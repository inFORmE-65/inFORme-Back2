package com.informe.informeapisb.src.news;

import com.informe.informeapisb.config.BaseException;
import com.informe.informeapisb.config.BaseResponse;
import com.informe.informeapisb.src.news.model.*;
import com.informe.informeapisb.utils.JwtService;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.rometools.rome.feed.rss.Channel;
import com.rometools.rome.feed.rss.Description;
import com.rometools.rome.feed.rss.Item;
import com.rometools.rome.feed.rss.Category;
import org.springframework.web.util.HtmlUtils;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

@RestController
@RequestMapping("/news")
public class newsController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final newsProvider newsProvider;
    @Autowired
    private final newsService newsService;
    @Autowired
    private final JwtService jwtService;

    public newsController(newsProvider newsProvider, newsService newsService, JwtService jwtService){
        this.newsProvider = newsProvider;
        this.newsService = newsService;
        this.jwtService = jwtService;
    }

    // rss 기능 (최신 정책 뉴스)
    @ResponseBody
    @GetMapping("/rss")
    public BaseResponse<GetFeedRes> feedReader() throws IOException {
        URL feedSource = new URL("https://www.korea.kr/rss/policy.xml");
        List<GetFeedData> data = new ArrayList<GetFeedData>();
        Pattern pattern1 = Pattern.compile("(<img[^>]+src\\s*=\\s*[\\\"']?([^>\\\"']+.jpg))[\\\"']?[^>]*>");
        Pattern pattern2 = Pattern.compile("(문의[^:]*:?(\\s|\\u00A0)*(\\S*))");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM월 dd일 k:m");

        try {
            SyndFeed feeder = new SyndFeedInput().build(new XmlReader(feedSource));
            for (Iterator iterator = feeder.getEntries().iterator(); iterator.hasNext();) {
                String imgUrl = null;
                String part = null;
                SyndEntry syndEntry = (SyndEntry) iterator.next();
                String Description = HtmlUtils.htmlUnescape(syndEntry.getDescription().getValue());
                Matcher matcher = pattern1.matcher(Description);
                if(matcher.find()){
                    imgUrl = matcher.group(2).trim();
                }
                Description = Description.replaceAll("<(/)?([a-zA-Z]*)(\\s[a-zA-Z]*=[^>]*)?(\\s)*(/)?>", "").replaceAll("<!--[^>]*>","").replaceAll("(\\n)+"," ").trim();
                matcher = pattern2.matcher(Description);
                if(matcher.find()){
                    part = matcher.group(3).trim();
                }
                data.add(new GetFeedData(syndEntry.getTitle(),part,syndEntry.getLink(),imgUrl,simpleDateFormat.format(syndEntry.getPublishedDate()),Description));
            }
            GetFeedRes getFeedRes = new GetFeedRes("정책정보","대한민국 정책포털 RSS 서비스",data);
            return new BaseResponse<>(getFeedRes);
        }catch (FeedException e) {
            throw new RuntimeException(e);
        }
    }

    // top10 뉴스 크롤링
    @ResponseBody
    @GetMapping("/top10")
    public BaseResponse<GetTopRes> top10() throws IOException {
        String url = "https://www.korea.kr/news/top50List.do";
        String baseUrl = "https://www.korea.kr";
        List<GetTopData> data = new ArrayList<GetTopData>();

        try{
            Connection conn = Jsoup.connect(url);
            Document document = conn.get();

            Elements selects = document.select("#container > div > article > div.article-content > div > div.rank10 > ul > li");
            for (Element select : selects) {
                data.add(new GetTopData(select.getElementsByClass("num").text(),select.getElementsByTag("strong").text(),baseUrl + select.getElementsByTag("a").attr("href"),select.getElementsByTag("img").attr("src")));
            }
            GetTopRes getTopRes = new GetTopRes(data);

            return new BaseResponse<>(getTopRes);
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
