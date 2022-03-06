Feature: a graphql endpoint for a library

  Scenario: we can create and retrieve a book
    Given that we post on "/graphql":
      """graphql
      mutation {
        createAuthor(author: {
          name: "J.R.R Tolkien"
          books: [{
            name: "Bilbo the hobbit"
          }]
        }) {
          id
        }
      }
      """

    When we post on "/graphql":
      """graphql
      query {
        firstBook( where: {name: {is: "Bilbo the hobbit" }}) {
          id name author {
            id name
          }
        }
      }
      """

    Then we receive:
      """json
      {
        "firstBook": {
          "id": 1,
          "name": "Bilbo the hobbit",
          "author": {
            "id": 1,
            "name": "J.R.R Tolkien"
          }
        }
      }
      """
