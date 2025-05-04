package ru.yandex.paging;


public record  Paging (
    int pageSize,
    int pageNumber,
    boolean hasNext,
    boolean hasPrevious
){}
