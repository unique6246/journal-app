package com.example.demo.sheduler;

import com.example.demo.cache.AppCache;
import com.example.demo.entity.Journal;
import com.example.demo.entity.User;
import com.example.demo.enums.Sentiment;
import com.example.demo.repo.UserIMPL;
import com.example.demo.service.EmailService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class UserScheduler {

    private final EmailService emailService;
    private final UserIMPL userIMPL;
    private final AppCache appCache;

    public UserScheduler(EmailService emailService, UserIMPL userIMPL, AppCache appCache) {
        this.emailService = emailService;
        this.userIMPL = userIMPL;
        this.appCache = appCache;
    }

//    @Scheduled(cron = "0 0 9 * * SUN")
    public void fetchUserAndSendMail(){
        List<User> userList = userIMPL.getUsersForSA();
        for(User user : userList){
            List<Journal> journalList = user.getJournalList();
            List<Sentiment> sentiments = journalList.stream().filter(x -> x.getDate().isAfter(LocalDateTime.now().minusDays(7))).map(Journal::getSentiment).collect(Collectors.toList());

            //most frequent sentiment
            Map<Sentiment,Integer> sentimentCounts=new HashMap<>();
            for(Sentiment sentiment : sentiments){
                if(sentiment!=null){
                    sentimentCounts.put(sentiment,sentimentCounts.getOrDefault(sentiment,0) + 1);
                }
            }
            Sentiment mostSentiment = null;
            int maxCount = 0;
            for (Map.Entry<Sentiment,Integer> entry : sentimentCounts.entrySet()){
                if(entry.getValue()>maxCount){
                    maxCount=entry.getValue();
                    mostSentiment = entry.getKey();
                }
            }
            if(mostSentiment!=null){
                emailService.sendNotification(user.getEmail(),"Sentimenty for last 7 days",mostSentiment.toString());
            }

        }
    }

    @Scheduled(cron = "0 0/10 * ? * *")
    public void clearAppCache(){
        appCache.init();
    }

}
