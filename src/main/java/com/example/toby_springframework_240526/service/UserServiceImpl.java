package com.example.toby_springframework_240526.service;

import com.example.toby_springframework_240526.dao.UserDao;
import com.example.toby_springframework_240526.domain.Level;
import com.example.toby_springframework_240526.domain.User;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

import java.util.List;


public class UserServiceImpl implements UserService{
    public final static int MIN_LOGINCOUNT_FOR_SILVER = 50;
    public final static int MIN_RECCOMEND_FOR_GOLD = 30;

    private final UserDao userDao;
    private MailSender mailSender;

    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    public void setMailSender(MailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void upgradeLevels() {
        List<User> all = userDao.getAll();

        for (User user : all) {
            if (canUpgradeLevel(user)) {
                upgradeLevel(user);
            }
        }
    }

    protected void upgradeLevel(User user) {
        user.upgradeLevel();
        userDao.update(user);
//        if (user.getEmail() != null) {
//            sendUpgradeEmail(user);
//        }
    }

    private void sendUpgradeEmail(User user) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setFrom("useradmin@ksug.org");
        mailMessage.setSubject("Upgrade 안내");
        mailMessage.setText("사용자님의 등급이 " + user.getLevel().name() +"로 업그레이드 되었습니다.");
        mailSender.send(mailMessage);
    }

    private boolean canUpgradeLevel(User user) {
        Level currentLevel = user.getLevel();
        switch (currentLevel) {
            case BASIC:
                return user.getLogin() >= MIN_LOGINCOUNT_FOR_SILVER;
            case SILVER:
                return user.getRecommend() >= MIN_RECCOMEND_FOR_GOLD;
            case GOLD:
                return false;
            default:
                throw new IllegalArgumentException("Unknown level:" + currentLevel);
        }
    }

    @Override
    public void add(User user) {
        if (user.getLevel() == null) {
            user.setLevel(Level.BASIC);
        }

        userDao.add(user);
    }

}
