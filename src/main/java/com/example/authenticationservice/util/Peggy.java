package com.example.authenticationservice.util;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Peggy<T> {
    private int page;
    private int limit;
    private int totalPage;
    private List<T> content;
}
