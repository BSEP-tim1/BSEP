import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NewCertificateAdminComponent } from './new-certificate-admin.component';

describe('NewCertificateAdminComponent', () => {
  let component: NewCertificateAdminComponent;
  let fixture: ComponentFixture<NewCertificateAdminComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ NewCertificateAdminComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(NewCertificateAdminComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
