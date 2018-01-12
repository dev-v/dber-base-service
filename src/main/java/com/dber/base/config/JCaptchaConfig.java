package com.dber.base.config;

import com.octo.captcha.CaptchaFactory;
import com.octo.captcha.component.image.backgroundgenerator.*;
import com.octo.captcha.component.image.color.ColorGenerator;
import com.octo.captcha.component.image.color.RandomListColorGenerator;
import com.octo.captcha.component.image.color.RandomRangeColorGenerator;
import com.octo.captcha.component.image.fontgenerator.RandomFontGenerator;
import com.octo.captcha.component.image.textpaster.DecoratedRandomTextPaster;
import com.octo.captcha.component.image.textpaster.SimpleTextPaster;
import com.octo.captcha.component.image.textpaster.textdecorator.LineTextDecorator;
import com.octo.captcha.component.image.textpaster.textdecorator.TextDecorator;
import com.octo.captcha.component.image.wordtoimage.ComposedWordToImage;
import com.octo.captcha.component.word.FileDictionary;
import com.octo.captcha.component.word.wordgenerator.DictionaryWordGenerator;
import com.octo.captcha.component.word.wordgenerator.RandomWordGenerator;
import com.octo.captcha.engine.GenericCaptchaEngine;
import com.octo.captcha.image.ImageCaptchaFactory;
import com.octo.captcha.image.fisheye.FishEyeFactory;
import com.octo.captcha.image.gimpy.GimpyFactory;
import com.octo.captcha.service.multitype.GenericManageableCaptchaService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.awt.*;

/**
 * <li>修改记录: ...</li>
 * <li>内容摘要: ...</li>
 * <li>其他说明: ...</li>
 *
 * @author dev-v
 * @version 1.0
 * @since 2018/1/10
 */
@Configuration
public class JCaptchaConfig {
    private static final int width = 270;
    private static final int height = 70;
    private static final int minFontSize = 30;
    private static final int maxFontSize = 50;
    private static final Color[] textColors = {Color.decode("#e6f7ff"), Color.decode("#b5f5ec"), Color.white, Color.decode("#d9f7be"), Color.decode("#fff566"), Color.decode("#f4ffb8")};
    private static final Color[] lineColors = {Color.blue, Color.lightGray, Color.pink, Color.BLACK};
    private static final Color[] backgroundColors = {Color.decode("#fa8c16"), Color.decode("#ad6800"), Color.decode("#876800"), Color.decode("#3f6600")};


    @Bean
    public GenericManageableCaptchaService captchaService() {
        GenericManageableCaptchaService captchaService = new GenericManageableCaptchaService(
                captchaEngine(),
                180,
                180000,
                90000);
        return captchaService;
    }

    @Bean
    public GenericCaptchaEngine captchaEngine() {
        CaptchaFactory[] factories = {gimpyFactory()};
        return new GenericCaptchaEngine(factories);
    }

//    public FishEyeFactory fishEyeFactory(){
//        EllipseBackgroundGenerator ellipseBackgroundGenerator=new EllipseBackgroundGenerator(width,height);
//        FishEyeFactory fishEyeFactory=new FishEyeFactory(ellipseBackgroundGenerator);
//        return fishEyeFactory;
//    }

    @Bean
    public GimpyFactory gimpyFactory() {
        return new GimpyFactory(wordGenerator(), wordToImage());
    }

    @Bean
    public RandomWordGenerator wordGenerator() {
        return new RandomWordGenerator("0123456789abcdefghijklmnopqrstuvwxyz");
    }

    @Bean
    public ComposedWordToImage wordToImage() {
        Font[] fonts = {new Font("Arial", 0, 15)};
        return new ComposedWordToImage(
                new RandomFontGenerator(minFontSize, maxFontSize, fonts),
//                new EllipseBackgroundGenerator(width, height),
//                new UniColorBackgroundGenerator(width, height),
                new FunkyBackgroundGenerator(width, height, new RandomListColorGenerator(backgroundColors)),
//                new GradientBackgroundGenerator(width,height,Color.lightGray,Color.darkGray),
//                new MultipleShapeBackgroundGenerator(width, height),
                textPaster());
    }

    public DecoratedRandomTextPaster textPaster() {
        TextDecorator[] textDecorators = {new LineTextDecorator(1,
                new RandomListColorGenerator(lineColors))};
        return new DecoratedRandomTextPaster(3, 4,
                new RandomListColorGenerator(textColors), textDecorators
        );
    }

}
