# Purpose
File to keep track of database entities specifications. 
Define fields, purpose of each entity, current and expected.
Used to brainstorm and communicate ideas behind initial implementation.

## Note on upkeep
This is likely to get outdated past the initial implementation of #4, #10.
We can remove this file if we don't find it useful to upkeep by that point.
Otherwise, we should update this and accurately keep version control information.

# Entities
## Accounts
### Description
Accounts can follow other Accounts. They can review products. 
They need to be able to log in, using a username and a password.

### Specifications
- (PK) Username: Uniquely identify account.
- Password: Used to authenticate account. Hashed (or see how authentication method wants us to store it)

- Follows: Many-to-Many Accounts association. Can have 0 follows, followers.
    No need to know followers, not sure if it's actually supposed to be one-to-many in that case? Don't think so.
- Reviews: One-to-Many Reviews association. Can have 0.

## Reviews
### Description
Reviews have a score associated (1 out of 5 stars, we can do halves to do 1/10). 
Reviews have a text description for rationale behind the score.
Reviews have an associated account who created the review.
Reviews are associated to a product.

### Specifications
- (PK1) Reviewer: One-to-One User association. Always have exactly 1 reviewer.
- (PK2) ReviewedProduct: One-to-One Product association. Always have exactly 1 reviewed product.

- Score: 1-10 int value (later converted to 1-5 star score with half increments)
- Rationale: String, expect a short paragraph. *Limit to 100 words ***Tentative limit**

- Q: Can we make the **primary key** (Reviewer, ReviewedProduct)? 
Can we enforce uniqueness + save a field at the same time doing that?

## Products
### Description
Products have reviews associated to them. They also display a score based on those reviews.
They have data given by the seller such as a name, a link, an image, and a description.

Having a unique name makes sense I think, we wouldn't want two sellers with the same product to duplicate a product entity. 
Instead, group them together under links. 
We don't care to store seller information as an entity, that's why it's explicitly a set and not an association.

### Specifications
- (PK) Name: String, short sentence. *Limit to 20 words ***Tentative limit**

- Price: Float, price of product.
- Description: String, short paragraph. *Limit to 250 words ***Tentative limit**
- Link: Set<String>, links to sellers. 1 long string allowed, check for link formatting.
- Image: Image file. Do format checks.

- Reviews: One-to-Many Reviews association. Can have 0.
- Score: Computed from reviews, not actually stored -> allows us to have different algorithms.
