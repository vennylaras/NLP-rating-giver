# -*- coding: utf-8 -*-
import scrapy


class GoodreadsSpider(scrapy.Spider):
	name = "goodreads"
	start_urls = [
		'file:///C:/xampp/htdocs/nlp-rating-giver/%234.txt',
	]

	def rate(self, x):
		return {
			'did not like it': 1,
			'it was ok': 2,
			'liked it': 3,
			'really liked it': 4,
			'it was amazing': 5,
		}.get(x, 0)

	def parse(self, response):
		for review in response.css("div.friendReviews") :
			texts = review.css("div.section div.review div.left div.reviewText span.readable span")
			yield{
				'rating' : self.rate(review.css("div.section div.review div.left div.reviewHeader span.staticStars span.staticStar::text").extract_first()),
				'text' : "".join(texts.xpath(".//text()").extract())
			}
