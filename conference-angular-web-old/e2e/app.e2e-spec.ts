import { Ngcli04Page } from './app.po';

describe('ngcli04 App', function() {
  let page: Ngcli04Page;

  beforeEach(() => {
    page = new Ngcli04Page();
  });

  it('should display message saying app works', () => {
    page.navigateTo();
    expect(page.getParagraphText()).toEqual('app works!');
  });
});
