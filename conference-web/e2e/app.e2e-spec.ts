import { ConferenceWebPage } from './app.po';

describe('conference-web App', function() {
  let page: ConferenceWebPage;

  beforeEach(() => {
    page = new ConferenceWebPage();
  });

  it('should display message saying app works', () => {
    page.navigateTo();
    expect(page.getParagraphText()).toEqual('conf works!');
  });
});
