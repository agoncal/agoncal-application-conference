import { browser, element, by } from 'protractor';

export class ConferenceWebPage {
  navigateTo() {
    return browser.get('/');
  }

  getParagraphText() {
    return element(by.css('conf-root h1')).getText();
  }
}
