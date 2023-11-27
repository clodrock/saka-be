package com.clodrock.sakabe.captcha;

import org.springframework.beans.factory.InitializingBean;

import cn.apiclub.captcha.Captcha;
import cn.apiclub.captcha.backgrounds.BackgroundProducer;
import cn.apiclub.captcha.backgrounds.TransparentBackgroundProducer;
import cn.apiclub.captcha.noise.CurvedLineNoiseProducer;
import cn.apiclub.captcha.noise.NoiseProducer;
import cn.apiclub.captcha.text.producer.DefaultTextProducer;
import cn.apiclub.captcha.text.producer.TextProducer;
import cn.apiclub.captcha.text.renderer.DefaultWordRenderer;
import cn.apiclub.captcha.text.renderer.WordRenderer;

public class CaptchaGenerator implements InitializingBean {

    private BackgroundProducer backgroundProducer = new TransparentBackgroundProducer();
    private TextProducer textProducer=new DefaultTextProducer();
    private WordRenderer wordRenderer=new DefaultWordRenderer();
    private NoiseProducer noiseProducer=new CurvedLineNoiseProducer();

    public Captcha createCaptcha(int width, int height){
        return new Captcha.Builder(width, height)
                .addBackground(backgroundProducer)
                .addText(textProducer,wordRenderer)
                .addNoise(noiseProducer).build();
    }

    @Override
    public void afterPropertiesSet() {
        if(this.backgroundProducer==null){
            this.backgroundProducer=new TransparentBackgroundProducer();
        }
        if(this.textProducer==null){
            this.textProducer=new DefaultTextProducer();
        }
        if(this.wordRenderer==null){
            this.wordRenderer= new DefaultWordRenderer();
        }
        if(this.noiseProducer==null){
            this.noiseProducer=new CurvedLineNoiseProducer();
        }

    }

}
