package com.luo.miaosha.service;

import com.luo.miaosha.dao.MiaoshaOrderMapper;
import com.luo.miaosha.dao.OrderMapper;
import com.luo.miaosha.domain.Goods;
import com.luo.miaosha.domain.MiaoshaOrder;
import com.luo.miaosha.domain.MiaoshaUser;
import com.luo.miaosha.domain.OrderInfo;
import com.luo.miaosha.redis.MiaoshaKey;
import com.luo.miaosha.redis.OrderKey;
import com.luo.miaosha.util.MD5Util;
import com.luo.miaosha.util.UUIDUtil;
import com.luo.miaosha.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Random;

@Service
public class MiaoshaOrderService {

    @Autowired
    private MiaoshaOrderMapper orderMapper;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private OrderService orderService;
    @Autowired
    private RedisService redisService;
    public MiaoshaOrder getByUserIdGoodsId(Integer userId, Integer goodsId) {
        MiaoshaOrder order = redisService.get(OrderKey.getMiaoshaORderByUidGid, "" + userId + ":" + goodsId, MiaoshaOrder.class);
        if (order != null) {
            return order;
        }
        MiaoshaOrder miaoshaOrder = orderMapper.getByUserIdGoodsId(userId, goodsId);
        redisService.set(OrderKey.getMiaoshaORderByUidGid, "" + userId + ":" + goodsId, miaoshaOrder);
        return miaoshaOrder;
    }

    @Transactional
    public OrderInfo miaosha(MiaoshaUser user, GoodsVo goods) {
        //减少 库存
        boolean success = goodsService.reduceStock(goods);
        if (success) {
            //同一个 用户可以 发送2个请求，秒杀2个同样的goods，创建2个订单
            //解决方法：卖超：创建user_id,goods_id的唯一索引在miaosha_order
            return orderService.createOrder(user, goods);
        }
        setGoodsOver(goods.getId());
        return null;
    }

    private void setGoodsOver(Integer goodsId) {
        redisService.set(MiaoshaKey.isGoodsOVer, "" + goodsId, true);
    }


    public Integer miaoshaResult(Integer userId, Integer goodsId) {
        MiaoshaOrder miaoshaOrder = getByUserIdGoodsId(userId, goodsId);
        if (miaoshaOrder != null) {
            return miaoshaOrder.getId();//秒杀成功
        } else {
            boolean isOver = getGoodsOver(goodsId);
            if (isOver) {
                return -1;//秒杀失败
            } else {
                return 0;//排队中
            }
        }
    }

    private boolean getGoodsOver(Integer goodsId) {
        return redisService.exist(MiaoshaKey.isGoodsOVer, goodsId + "");

    }

    public boolean checkPath(MiaoshaUser user, Integer goodsId, String path) {
        if (user == null || path == null) {
            return false;
        }
        String result = redisService.get(MiaoshaKey.getMiaoshaPath, user.getId() + "_" + goodsId, String.class);
        return path.equals(result);

    }

    public String createPath(MiaoshaUser user, Integer goodsId) {
        String str = MD5Util.md5(UUIDUtil.uuid() + "123");
        redisService.set(MiaoshaKey.getMiaoshaPath, user.getId() + "_" + goodsId, str);
        return str;
    }

    public BufferedImage createVerifyCode(MiaoshaUser user, Integer goodsId) {
        if (user == null || goodsId == null) {
            return null;
        }

        int width = 80;
        int height = 20;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        Graphics g = image.getGraphics();
        g.setColor(new Color(0xDCDCDC));
        g.fillRect(0, 0, width, height);

        g.setColor(Color.black);
        g.drawRect(0, 0, width - 1, height - 1);
        Random r = new Random();

        for (int i = 0; i < 50; i++) {
            int x = r.nextInt(width);
            int y = r.nextInt(height);
            g.drawOval(x, y, 0, 0);
        }
        String verifyCode = genVerifyCode(r);

        g.setColor(new Color(0, 100, 0));
        g.setFont(new Font("Candara", Font.BOLD, 25));
        g.drawString(verifyCode, 8, 24);
        g.dispose();
        int rnd = calc(verifyCode);
        redisService.set(MiaoshaKey.getVefiryCode, user.getId() + "" + goodsId, rnd);
        return image;
    }

    private int calc(String verifyCode) {
        ScriptEngineManager engineManager = new ScriptEngineManager();
        ScriptEngine js = engineManager.getEngineByName("JavaScript");
        try {
            return (int) js.eval(verifyCode);
        } catch (ScriptException e) {
            e.printStackTrace();
            return 0;
        }


    }

    private static char[] ops = new char[]{'+', '-', '*'};

    private String genVerifyCode(Random r) {
        int a = r.nextInt(10);
        int b = r.nextInt(10);
        int c = r.nextInt(10);
        char op = ops[r.nextInt(3)];
        return a + op + b + op + c + "";

    }


    public boolean checkVerifyCode(MiaoshaUser user, Integer goodsId, int verifyCode) {

        if (user == null || goodsId == null) {
            return false;
        }
        Integer result = redisService.get(MiaoshaKey.getVefiryCode, user.getId() + "" + goodsId, Integer.class);
        if (result == null || result - verifyCode != 0) {
            return false;
        }
        redisService.delete(MiaoshaKey.getVefiryCode, user.getId() + "" + goodsId);
        return true;
    }
}
