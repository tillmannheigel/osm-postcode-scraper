package de.tillmannheigel.osmpostcodescraper;

import java.io.IOException;

import javax.annotation.PostConstruct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import de.tillmannheigel.osmpostcodescraper.scraper.Scraper;

import lombok.RequiredArgsConstructor;

@SpringBootApplication
@RequiredArgsConstructor
public class OsmPostcodeScraperApplication {

	private final Scraper scraper;

	public static void main(String[] args) {
		SpringApplication.run(OsmPostcodeScraperApplication.class, args);
	}

	@PostConstruct
	public void scrape() throws IOException {
		scraper.scrape();
	}

}
