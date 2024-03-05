package com.example.gdsc.service;

import com.example.gdsc.Dto.MemberDto;
import org.apache.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class WebClientService {
    //일반적
    public String getName(){
        WebClient webClient = WebClient.builder()
                .baseUrl("http://localhost:9090")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
        return webClient.get()
                .uri("/api/v1/crud-api")
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
    // ResponseEntity타입으로 응답
    public String getNameWithPathVariable(){
        WebClient webClient = WebClient.create("http://localhost:9090");

        ResponseEntity<String> responseEntity = webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/v1/crud-api/{name}")
                .build("Flature"))
                .retrieve().toEntity(String.class).block();

        return responseEntity.getBody();
    }
    //PathVariable 값을 추가해 요청을 보내는 예제
    public String getNameWithParameter(){
        WebClient webClient = WebClient.create("http://localhost:9090");

        return webClient.get().uri(uriBuilder -> uriBuilder.path("/api/v1/crud-api")
                .queryParam("name","Flature")
                        .build())
                .exchangeToMono(clientResponse -> {
                    if(clientResponse.statusCode().equals(HttpStatus.OK)){
                        return clientResponse.bodyToMono(String.class);
                    }else{
                        return clientResponse.createException().flatMap(Mono::error);
                    }
                })
                .block();
        /*이렇게 작성 가능
        ResponseEntity<String> responseEntity = webClient.get()
                .uri("/api/v1/crud-api/{name}","Flature")
                .retrieve()
                .toEntity(String.class)
                .block();

         */
    }
    //WebCLient 복제
    //WebClient clone = webClient.mutate().build()

    public ResponseEntity<MemberDto> postWithParamAndBody(){
        WebClient webClient = WebClient.builder()
                .baseUrl("http://localhost:9090")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        MemberDto memberDto = new MemberDto();
        memberDto.setName("flature!");
        memberDto.setEmail("flature@gmail.com");
        memberDto.setOrganization("Spring");

        return webClient.post().uri(uriBuilder -> uriBuilder.path("/api/v1/crud-api")
                .queryParam("name","Flature")
                .queryParam("email","flature@naver.com")
                .queryParam("organization","naver")
                .build())
                .bodyValue(memberDto)
                .retrieve()
                .toEntity(MemberDto.class)
                .block();

    }
    public ResponseEntity<MemberDto> postWithHeader(){
        WebClient webClient = WebClient.builder()
                .baseUrl("http://localhost:9090")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        MemberDto memberDto = new MemberDto();
        memberDto.setName("flature!");
        memberDto.setEmail("flature@naver.com");
        memberDto.setOrganization("Spring");

        return webClient.post()
                .uri(uriBuilder -> uriBuilder.path("/api/v1/crud-api/add-header")
                        .build())
                .bodyValue(memberDto)
                .header("my-header","GDSC API")
                .retrieve()
                .toEntity(MemberDto.class)
                .block();

    }

}
