# -*- coding: utf-8 -*-
import scrapy


class GoodreadsSpider(scrapy.Spider):
	name = "goodreads"
	start_urls = [
		'https://www.goodreads.com/book/show/3.Harry_Potter_and_the_Sorcerer_s_Stone',
	]

	def parse(self, response):
		for review in response.css("div.friendReviews") :
			yield{
				'text' : review.css("div.section").extract()
			}
        