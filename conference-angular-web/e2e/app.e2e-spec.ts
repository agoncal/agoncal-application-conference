import { ConferenceAngularWebPage } from './app.po';

describe('conference-angular-web App', () => {
  let page: ConferenceAngularWebPage;

  beforeEach(() => {
    page = new ConferenceAngularWebPage();
  });

  it('should display message saying app works', () => {
    page.navigateTo();
    expect(page.getParagraphText()).toEqual('app works!');
  });
});
