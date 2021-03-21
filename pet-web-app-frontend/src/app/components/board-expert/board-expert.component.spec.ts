import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BoardExpertComponent } from './board-expert.component';

describe('BoardExpertComponent', () => {
  let component: BoardExpertComponent;
  let fixture: ComponentFixture<BoardExpertComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ BoardExpertComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(BoardExpertComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
