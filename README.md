# OrderApi
A RESTful service that manages order creation, processing, tracking, and history within the eCommerce application.

## 1. Create a New Order
**Endpoint:** `POST orderApi/orders`
**Description:** Creates a new order with customer and item details.

**Request**
 ```json
{
  "customerId": 42,
  "orderItems": [
    {
      "productId": 2,
      "productSku": "f9c3bc0c-371a-4420-ab6f-efed11723380",
      "productName": "Second Product",
      "quantity": 1,
      "unitPrice": 1.2,
      "discountPercent": 10.0,
      "taxPercent": 20.0,
      "shippingDetail": {
        "weight": 12.0,
        "dimensions": "20*23*28",
        "courier": "Next Door"
      }
    },
    {
      "productId": 1,
      "productSku": "599e13c6-9e0f-4968-8e93-9bd04fe9ee5a",
      "productName": "First Product",
      "quantity": 5,
      "unitPrice": 42.0,
      "discountPercent": 4.0,
      "taxPercent": 21.0,
      "shippingDetail": {
        "weight": 21,
        "dimensions": "21*12*84",
        "courier": "DHLC"
      }
    }
  ]
}
```
**Response**
`Order: EC-42-20250420-1669 has been created successfully.`

## 2. Retrieve All Orders
**Endpoint:** `GET orderApi/orders`
**Description:** Retrieve all orders with corresponding line items.

**Request** Blank request

**Response**
```json
[
  {
    "id": 1,
    "customerId": 42,
    "orderNumber": "EC-42-20250420-1269",
    "totalAmount": 247.01999999999998,
    "createdAt": "2025-04-20T17:59:57.036233",
    "updatedAt": "2025-04-20T17:59:57.036233",
    "orderStatus": "PENDING",
    "orderItems": [
      {
        "id": 1,
        "productId": 2,
        "productName": "Second Product",
        "quantity": 1,
        "unitPrice": 1.2,
        "grossPrice": 1.3199999999999998,
        "deliveryDate": "2025-04-22",
        "productSku": "f9c3bc0c-371a-4420-ab6f-efed11723380",
        "discountPercent": 10.0,
        "variantId": null,
        "taxPercent": 20.0,
        "finalPrice": 1.3199999999999998,
        "status": "PENDING",
        "createdAt": "2025-04-20T17:59:57.036233",
        "updatedAt": "2025-04-20T17:59:57.036233",
        "warehouseId": 0,
        "shippingDetail": {
          "weight": 12.0,
          "dimensions": "20*23*28",
          "courier": "Next Door"
        },
        "giftMessage": null,
        "returnEligibility": false,
        "loyaltyPointsEarned": 0,
        "gift": false
      },
      {
        "id": 2,
        "productId": 1,
        "productName": "First Product",
        "quantity": 5,
        "unitPrice": 42.0,
        "grossPrice": 245.7,
        "deliveryDate": "2025-04-27",
        "productSku": "599e13c6-9e0f-4968-8e93-9bd04fe9ee5a",
        "discountPercent": 4.0,
        "variantId": null,
        "taxPercent": 21.0,
        "finalPrice": 49.14,
        "status": "PENDING",
        "createdAt": "2025-04-20T17:59:57.036233",
        "updatedAt": "2025-04-20T17:59:57.036233",
        "warehouseId": 0,
        "shippingDetail": {
          "weight": 21.0,
          "dimensions": "21*12*84",
          "courier": "DHLC"
        },
        "giftMessage": null,
        "returnEligibility": false,
        "loyaltyPointsEarned": 0,
        "gift": false
      }
    ]
  }
]
```
## 3. Update Order Status
**Endpoint:** `PUT orderApi/orders`
**Description:** Update Order Status.

**Request**
```json
{
    "orderNumber": "EC-42-20250420-1189",
    "orderStatus": "PAID"
}
```

**Response**
```Order: EC-42-20250420-1189 status has been successfully updated to PAID.```