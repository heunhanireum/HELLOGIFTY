package com.a705.hellogifty.api.service;

import com.a705.hellogifty.api.domain.entity.Gifticon;
import com.a705.hellogifty.api.domain.entity.TradePost;
import com.a705.hellogifty.api.domain.entity.User;
import com.a705.hellogifty.api.domain.enums.TradeState;
import com.a705.hellogifty.api.dto.trade_post.TradePostDetailResponseDto;
import com.a705.hellogifty.api.dto.trade_post.TradePostEditRequestDto;
import com.a705.hellogifty.api.dto.trade_post.TradePostRequestDto;
import com.a705.hellogifty.api.repository.GifticonRepository;
import com.a705.hellogifty.api.repository.TradePostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.File;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class TradeService {

    private final TradePostRepository tradePostRepository;
    private final GifticonRepository gifticonRepository;

    @Transactional
    public TradePostDetailResponseDto tradePostDetail(User user, Long tradePostId) {
        String defaultPath = System.getProperty("user.dir")+File.separator+"static"+File.separator+"img"+File.separator+"gifticonCropImg"+File.separator;
        TradePost tradePost = tradePostRepository.findById(tradePostId).get();
        return new TradePostDetailResponseDto(tradePost, defaultPath);
    }

    @Transactional
    public void tradePostCreate(User user, TradePostRequestDto tradePostRequestDto, File img) {
        Gifticon gifticon = gifticonRepository.findById(tradePostRequestDto.getGifticonId()).get();
        TradePost tradePost = TradePost.builder().user(user)
                .gifticon(gifticon)
                .title(tradePostRequestDto.getTitle())
                .content(tradePostRequestDto.getContent())
                .price(tradePostRequestDto.getPrice())
                .tradeState(TradeState.ONSALE)
                .img(img.getName())
                .createdAt(LocalDate.now())
                .modifiedAt(LocalDate.now()).build();

        tradePostRepository.save(tradePost);
    }

    @Transactional
    public String getOriginalImgName(User user, Long gifticonId) {
        Gifticon gifticon = gifticonRepository.findById(gifticonId).get();
        return gifticon.getImg();
    }

    @Transactional
    public void tradePostEdit(User user, Long tradePostId, TradePostEditRequestDto tradePostEditRequestDto) {
        TradePost tradePost = tradePostRepository.findById(tradePostId).get();
        tradePost.update(tradePostEditRequestDto);
    }

    @Transactional
    public void tradePostDelete(User user, Long tradePostId) {
        tradePostRepository.deleteById(tradePostId);
    }
}
