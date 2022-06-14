import { CreateSelfSignedCertificate } from './create-self-signed-certificate';

describe('CreateSelfSignedCertificate', () => {
  it('should create an instance', () => {
    expect(new CreateSelfSignedCertificate()).toBeTruthy();
  });
});
