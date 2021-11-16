package com.sam.android_showcase.network

import com.apollographql.apollo.ApolloClient

private var instance: ApolloClient? = null

fun apolloClient(): ApolloClient {
    if (instance != null) {
        return instance!!
    }

    instance = ApolloClient.builder()
        .serverUrl("https://graphqlzero.almansi.me/api")
        .build()

    return instance!!
}
